package concurent.student.second;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Base {

    private static final int STARTER_PEASANT_NUMBER = 5;
    private static final int PEASANT_NUMBER_GOAL = 10;
    private static final int FOOTMAN_NUMBER_GOAL = 10;
    
    private static final int FARM_GOAL = 3;
    private static final int BLACKSMITH_GOAL = 1;
    private static final int LUMBERMILL_GOAL = 1;
    private static final int BARRACK_GOAL = 1;

    // lock to ensure only one unit can be trained at one time
    private final ReentrantLock trainingLock = new ReentrantLock();

    private final String name;
    private final Resources resources = new Resources();
    private final List<Peasant> peasants = Collections.synchronizedList(new LinkedList<>());
    private final List<Footman> footmen = Collections.synchronizedList(new LinkedList<>());
    private final List<Building> buildings = Collections.synchronizedList(new LinkedList<>());
    private final List<Personnel> army = Collections.synchronizedList(new LinkedList<>());

    public Base(String name) {
        this.name = name;

        // TODO Create the initial 5 peasants - Use the STARTER_PEASANT_NUMBER constant
        for(int i = 0; i < STARTER_PEASANT_NUMBER; i++) {
            // TODO Use the createPeasant() method
            peasants.add(createPeasant());
        }

        // TODO 3 of them should mine gold        
        peasants.get(0).startMining();
        peasants.get(1).startMining();
        peasants.get(2).startMining();

        // TODO 1 of them should cut tree
        peasants.get(3).startCuttingWood();

        // TODO 1 should do nothing
    }

    public void startPreparation() {
        // TODO Start the building and training preparations on separate threads
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        
        // TODO Tip: use the hasEnoughBuilding method
        // TODO Build 3 farms - use getFreePeasant() method to see if there is a peasant without any work
        executorService.submit(() -> {
            
            try {
                while(!hasEnoughBuilding(UnitType.FARM, FARM_GOAL)) {
                    Peasant freePeasant = getFreePeasant();
                    if(freePeasant != null) {
                        freePeasant.tryBuilding(UnitType.FARM);
                    }
                }
            }
            catch(Throwable e) {
                e.printStackTrace();
            }
        });

        // TODO Build a lumbermill - use getFreePeasant() method to see if there is a peasant without any work
        executorService.submit(() -> {
            
            try {
                while(!hasEnoughBuilding(UnitType.LUMBERMILL, LUMBERMILL_GOAL)) {
                    Peasant freePeasant = getFreePeasant();
                    if(freePeasant != null) {
                        freePeasant.tryBuilding(UnitType.LUMBERMILL);
                    }
                }
            }
            catch(Throwable e) {
                e.printStackTrace();
            }
        });

        // TODO Build a blacksmith - use getFreePeasant() method to see if there is a peasant without any work
        executorService.submit(() -> {
            
            try {
                while(!hasEnoughBuilding(UnitType.BLACKSMITH, BLACKSMITH_GOAL)) {
                    Peasant freePeasant = getFreePeasant();
                    if(freePeasant != null) {
                        freePeasant.tryBuilding(UnitType.BLACKSMITH);
                    }
                }
            }
            catch(Throwable e) {
                e.printStackTrace();
            }
        });

        // TODO Create remaining 5 peasants - Use the PEASANT_NUMBER_GOAL constant
        executorService.submit(() -> {
            
            try {
                while(peasants.size() < PEASANT_NUMBER_GOAL) {

                    // TODO Use the createPeasant() method
                    Peasant freePeasant = createPeasant();
                    if(freePeasant != null) {
                        
                        // TODO 2 of them should cut tree
                        if (peasants.size() < 6) {
                            freePeasant.startCuttingWood();
                        } // TODO 5 of them should mine gold
                        else if (peasants.size() >= 6 && peasants.size() < 8) {
                            freePeasant.startMining();
                        }

                        // TODO 3 of them should do nothing
                        peasants.add(freePeasant);
                    }
                }
            }
            catch(Throwable e) {
                e.printStackTrace();
            }
        });

        // Creating footmen
        executorService.submit(() -> {

            try {
                while(footmen.size() < FOOTMAN_NUMBER_GOAL) {

                    Footman newFootman = createFootman();
                    
                    if(newFootman != null) {
                        footmen.add(newFootman);
                    }
                }
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        });
        
        // TODO Build a barracks - use getFreePeasant() method to see if there is a peasant without any work
        
        executorService.submit(() -> {

            try {
                while (!hasEnoughBuilding(UnitType.BARRACKS, BARRACK_GOAL)) {
                    Peasant freePeasant = getFreePeasant();
                    if (freePeasant != null) {
                        freePeasant.tryBuilding(UnitType.BARRACKS);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        
        executorService.shutdown();     
        
        // TODO Wait for all the necessary preparations to finish
        
        try {
            executorService.awaitTermination(100, TimeUnit.SECONDS);
        }
        catch(InterruptedException ex) {
        }
        
        // TODO Stop harvesting with the peasants once everything is ready
        peasants.forEach((p) -> p.stopHarvesting());
        
        System.out.println(this.name + " finished creating a base");
        System.out.println(this.name + " peasants: " + this.peasants.size());
        System.out.println(this.name + " footmen: " + this.footmen.size());
        for(Building b : buildings){
            System.out.println(this.name + " has a  " + b.getUnitType().toString());
        }
    }

    /**
     * Assemble the army - call the peasants and footmen to arms
     * @param latch
     */
    public void assembleArmy(CountDownLatch latch){
        
        // TODO Add the peasants and footmen to the army
        for(int i = 0; i < peasants.size(); i++) {
            army.add(peasants.get(i));
        }
        
        for(int i = 0; i < footmen.size(); i++) {
            army.add(footmen.get(i));
        }
        
        System.out.println(this.name + " is ready for war");
        // the latch is used to keep track of both factions
        latch.countDown();
    }

    /**
     * Starts a war between the two bases.
     *
     * @param enemy Enemy base's personnel
     * @param warLatch Latch to make sure they attack at the same time
     */
    public void goToWar(List<Personnel> enemy, CountDownLatch warLatch){
        // This is necessary to ensure that both armies attack at the same time
        warLatch.countDown();
        try {
            // Waiting for the other army to be ready for war
            warLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ExecutorService executorService = Executors.newFixedThreadPool(army.size());
        
        // TODO Start attacking the enemy with every soldier on a separate thread
        
        for(int i = 0; i < army.size(); i++) {
            int _i = i;
            executorService.submit(() -> {
                army.get(_i).startWar(enemy);
            });
        }
        
        // TODO Wait until the fight is resolved
        
        executorService.shutdown();
        
        try {
            executorService.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
        }

        // If our army has no personnel, we failed
        if(army.isEmpty()){
            System.out.println(this.name + " has lost the fight");
        } else {
            System.out.println(this.name + " has won the fight");
        }
    }

    /**
     * Resolves the event when a personnel dies;
     * Remove it from the army and update the capacity.
     * @param p The fallen personnel
     */
    public void signalPersonnelDeath(Personnel p) {
        
        // TODO Update resource capacity (this personnel no longer requires the food obviously)
        resources.updateCapacity(-p.getUnitType().foodCost);
        
        // TODO Remove from army (and any other container)
        army.remove(p);
        
        if(p.getUnitType() == UnitType.PEASANT) {
            peasants.remove(p);
        }
        else {
            footmen.remove(p);
        }
        
        System.out.println(this.name + " has lost a " + p.getUnitType().toString());
    }

    /**
     * Returns a peasants that is currently free.
     * Being free means that the peasant currently isn't harvesting or building.
     *
     * @return Peasant object, if found one, null if there isn't one
     */
    private Peasant getFreePeasant() {
        
        for(int i = 0; i < peasants.size(); i++) {
            if(peasants.get(i).isFree()) {
                return peasants.get(i);       
            }
        }
        
        return null;
    }

    /**
     * Creates a peasant.
     * A peasant could only be trained if there are sufficient
     * gold, wood and food for him to train.
     *
     * At one time only one Peasant can be trained.
     *
     * @return The newly created peasant if it could be trained, null otherwise
     */
    private Peasant createPeasant() {
        
        Peasant result;
        
        if(resources.canTrain(UnitType.PEASANT.goldCost, UnitType.PEASANT.woodCost, UnitType.PEASANT.foodCost)) {
            
            // TODO Remember that at one time only one peasant can be trained
            trainingLock.lock();
            
            try {
                if(resources.getCapacityLimit() >= resources.getCapacity() + UnitType.PEASANT.foodCost) {

                    // TODO 1: Sleep as long as it takes to create a peasant - use sleepForMsec() method
                    sleepForMsec(UnitType.PEASANT.buildTime);

                    // TODO 2: Remove costs
                    resources.removeCost(UnitType.PEASANT.goldCost, UnitType.PEASANT.woodCost);

                    // TODO 3: Update capacity
                    resources.updateCapacity(UnitType.PEASANT.foodCost);

                    // TODO 4: Use the Peasant class' createPeasant method to create the new Peasant
                    result = Peasant.createPeasant(this);
                    return result;
                }
            }
            finally {
                trainingLock.unlock();
            } 
        }
        return null;
    }

    private Footman createFootman(){
        
        Footman result;
        
        if(resources.canTrain(UnitType.FOOTMAN.goldCost, UnitType.FOOTMAN.woodCost, UnitType.FOOTMAN.foodCost)) {
            
            trainingLock.lock();
            
            try {
                if(resources.getCapacityLimit() >= resources.getCapacity() + UnitType.FOOTMAN.foodCost) {

                    sleepForMsec(UnitType.FOOTMAN.buildTime);

                    resources.removeCost(UnitType.FOOTMAN.goldCost, UnitType.FOOTMAN.woodCost);

                    resources.updateCapacity(UnitType.FOOTMAN.foodCost);

                    result = Footman.createFootman(this);
                    return result;
                }
            }
            finally {
                trainingLock.unlock();
            } 
        }
        return null;
    }

    public Resources getResources(){
        return this.resources;
    }

    public List<Personnel> getArmy(){
        return this.army;
    }

    public List<Building> getBuildings(){
        return this.buildings;
    }

    public String getName(){
        return this.name;
    }

    /**
     * Helper method to determine if a base has the required number of a certain building.
     *
     * @param unitType Type of the building
     * @param required Number of required amount
     * @return true, if required amount is reached (or surpassed), false otherwise
     */
    private boolean hasEnoughBuilding(UnitType unitType, int required) {
        
        // TODO check in the buildings list if the type has reached the required amount
        int counter = 0;
        
        for(int i = 0; i < buildings.size(); i++) {
            if(buildings.get(i).getUnitType() == unitType) {
                ++counter;
            }
        }
        
        return counter >= required;
    }

    private static void sleepForMsec(int sleepTime) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }
}
