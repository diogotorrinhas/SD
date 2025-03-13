package heist.concurrent;

import heist.Configuration;
import heist.thief.OrdinaryThief;

/**
 * Main class.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, Exception {
        GeneralRepository repository = new GeneralRepository(new Configuration());
        OrdinaryThief[] thieves = repository.getOrdinaryThieves();
        repository.getMasterThief().start();
        for (int i = 0; i < thieves.length; i++) {
            thieves[i].start();
        }

//        for (int i = 0; i < thieves.length; i++) {
//            try {
//                // wait for all Ordinary Thief threads to finish
//                thieves[i].join();
//            } catch (InterruptedException e) {
//
//            }
//            System.out.println("The ORDINARY THIEF " + i + " has terminated.");
//        }
//        while (repository.getMasterThief().isAlive ())
//        {
//            repository.getMasterThief().interrupt ();
//            Thread.yield();
//        }
//        try
//        {
//            //wait for master thief to finish
//            repository.getMasterThief().join ();
//        }
//        catch (InterruptedException e) {}
//        System.out.println("The MASTER THIEF has terminated");
    }
}
