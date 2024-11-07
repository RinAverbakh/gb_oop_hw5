package service;

import model.FamilyTree;
import model.Person;
import view.TreeView;
import view.TreeViewInt;

import java.io.IOException;

public class CommandManager {
    private FamilyTree<Person> familyTree;
    private FileOperations<Person> fileOps;
    private TreeViewInt view;

    public CommandManager(FamilyTree<Person> familyTree) {
        this.familyTree = familyTree;
        this.fileOps = new FileOperationsImpl<>();
        this.view = new TreeView();
    }

    public void start() {
        while (true) {
            
            this.view.displayMessage("Введите команду (add, list, sortByName, sortByBirthYear, save, load, exit):");
            String command = this.view.getUserInput();

            switch (command) {

                case "add":
                    addPerson();
                    break;

                case "list":
                    listPeople();
                    break;

                case "sortByName":
                    familyTree.sortByName();
                    listPeople();
                    break;

                case "sortByBirthYear":
                    familyTree.sortByBirthYear();
                    listPeople();
                    break;

                case "save":
                    save();
                    break;

                case "load":
                    load();
                    break;

                case "exit":
                    return;
                default:
                this.view.displayMessage("Неизвестная команда");
            }
        }
    }

    private void addPerson() {
        this.view.displayMessage("Введите имя:");
        String name = this.view.getUserInput();

        this.view.displayMessage("Введите год рождения:");
        int birthYear = Integer.parseInt(this.view.getUserInput());

        Person person = new Person(name, birthYear);
        familyTree.addPerson(person);
        this.view.displayMessage("Человек добавлен в дерево.");
    }

    private void listPeople() {
        for (Person person : familyTree) {
            this.view.displayMessage(person.getName() + ", родился(ась) в " +
                    person.getBirthYear());
        }
    }

    private void save(){
        try {
            fileOps.saveToFile(familyTree, "familyTree.dat");
            this.view.displayMessage("Family tree saved to file.");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(){
        // Загружаем генеалогическое древо из файла
        FamilyTree<Person> loadedFamilyTree = null;
        try {
            loadedFamilyTree = fileOps.loadFromFile("familyTree.dat");
            this.view.displayMessage("Дерево выгруженно из файла");
        } 
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Проверяем, что древо загрузилось правильно
        if (loadedFamilyTree != null) {
            for (Person person : loadedFamilyTree) {
                this.view.displayMessage("Loaded person: " + person.getName() + ", born in " + person.getBirthYear());
            }
        }
        
        this.familyTree.addPeople(loadedFamilyTree);
    }
}
