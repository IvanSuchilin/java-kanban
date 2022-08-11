package main;

import main.manager.*;

import main.task.*;



public class Main {



    public static void main(String[] args) throws CloneNotSupportedException {


    }



    private static void printInfo() {

        TaskManager tasksManager = Managers.getDefault();

        System.out.println(tasksManager.getHistory());

        System.out.println("Размер истории: " + tasksManager.getHistory().size());

    }

}