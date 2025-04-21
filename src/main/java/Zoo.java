package src.main.java;

public class Zoo {
    private int animals;
    private boolean isOpen;
    private String name;
    private ZooKeeper zooKeeper;

    public Zoo(String name, ZooKeeper zooKeeper) {
        this.name = name;
        this.animals = 0;
        this.isOpen = false;
        this.zooKeeper = zooKeeper;
    }

    public void addAnimal() {
        // Se o ZooKeeper for nulo, não permite operações
        if (zooKeeper != null && zooKeeper.canAddAnimal()) {
            animals++;
        }
    }

    public void removeAnimal() {
        // Se o ZooKeeper for nulo, não permite operações
        if (zooKeeper != null && animals > 0 && zooKeeper.canRemoveAnimal()) {
            animals--;
        }
    }

    public int getAnimals() {
        return animals;
    }

    public void open() {
        // Se o ZooKeeper for nulo, não abre o zoológico
        if (zooKeeper != null && zooKeeper.isAvailable()) {
            isOpen = true;
        }
    }

    public void close() {
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getName() {
        return name;
    }
}
