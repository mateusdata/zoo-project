package src.test.java;

import org.junit.Test;       // JUnit
import org.junit.Before;     // JUnit
import src.main.java.Zoo;
import src.main.java.ZooKeeper;

import static org.junit.Assert.*;          // JUnit
import static org.mockito.Mockito.*;         // Mockito

public class ZooTest {
    private ZooKeeper mockZooKeeper;

    @Before
    public void setup() {
        // Configura o mock do ZooKeeper usando Mockito
        mockZooKeeper = mock(ZooKeeper.class);
        when(mockZooKeeper.canAddAnimal()).thenReturn(true);
        when(mockZooKeeper.canRemoveAnimal()).thenReturn(true);
        when(mockZooKeeper.isAvailable()).thenReturn(true);
    }

    // ----- Testes usando apenas JUnit (verificação direta sem mock de interações) -----
    
    @Test
    public void testZooStartsWithZeroAnimals() {
        Zoo zoo = new Zoo("Zero Animals Zoo", mockZooKeeper);
        assertEquals(0, zoo.getAnimals());
    }

    @Test
    public void testZooNameReturnsCorrectName() {
        Zoo zoo = new Zoo("Correct Name Zoo", mockZooKeeper);
        assertEquals("Correct Name Zoo", zoo.getName());
    }

    @Test
    public void testCloseZooSetsOpenToFalse() {
        Zoo zoo = new Zoo("Close Test Zoo", mockZooKeeper);
        zoo.open();
        zoo.close();
        assertFalse(zoo.isOpen());
    }

    @Test
    public void testAddMultipleAnimalsIncrementsCorrectly() {
        Zoo zoo = new Zoo("Multiple Add Zoo", mockZooKeeper);
        zoo.addAnimal();
        zoo.addAnimal();
        zoo.addAnimal();
        assertEquals(3, zoo.getAnimals());
    }

    @Test
    public void testRemoveMultipleAnimalsDecrementsCorrectly() {
        Zoo zoo = new Zoo("Multiple Remove Zoo", mockZooKeeper);
        zoo.addAnimal();
        zoo.addAnimal();
        zoo.removeAnimal();
        assertEquals(1, zoo.getAnimals());
    }
    
    @Test
    public void testZooWithoutKeeperDisablesOperations() {
        // Teste JUnit: comportamento quando ZooKeeper é nulo
        Zoo zoo = new Zoo("Zoo Without Keeper", null);
        zoo.addAnimal();
        zoo.removeAnimal();
        assertEquals(0, zoo.getAnimals());
        assertFalse(zoo.isOpen());
    }

    // ----- Testes usando Mockito (verificando interações com ZooKeeper) -----

    @Test
    public void testAddAnimalWhenPermitted() {
        Zoo zoo = new Zoo("Adding Animals Zoo", mockZooKeeper);
        zoo.addAnimal();
        assertEquals(1, zoo.getAnimals());
        verify(mockZooKeeper).canAddAnimal();
    }

    @Test
    public void testAddAnimalWhenNotPermitted() {
        when(mockZooKeeper.canAddAnimal()).thenReturn(false);
        Zoo zoo = new Zoo("Not Permitted Zoo", mockZooKeeper);
        zoo.addAnimal();
        assertEquals(0, zoo.getAnimals());
        verify(mockZooKeeper).canAddAnimal();
    }

    @Test
    public void testRemoveAnimalWhenPermitted() {
        Zoo zoo = new Zoo("Removing Animals Zoo", mockZooKeeper);
        zoo.addAnimal();
        zoo.removeAnimal();
        assertEquals(0, zoo.getAnimals());
        verify(mockZooKeeper).canRemoveAnimal();
    }

    @Test
    public void testRemoveAnimalWhenNotPermitted() {
        when(mockZooKeeper.canRemoveAnimal()).thenReturn(false);
        Zoo zoo = new Zoo("Cannot Remove Zoo", mockZooKeeper);
        zoo.addAnimal();
        zoo.removeAnimal();
        assertEquals(1, zoo.getAnimals());
        verify(mockZooKeeper).canRemoveAnimal();
    }
    
    @Test
    public void testZooKeeperInteractionsDuringOperations() {
        Zoo zoo = new Zoo("Keeper Interaction Zoo", mockZooKeeper);
        zoo.open();
        zoo.addAnimal();
        zoo.removeAnimal();
        verify(mockZooKeeper).isAvailable();
        verify(mockZooKeeper).canAddAnimal();
        verify(mockZooKeeper).canRemoveAnimal();
    }
    
    @Test
    public void testZooOpensWhenKeeperIsAvailable() {
        Zoo zoo = new Zoo("Open Zoo", mockZooKeeper);
        zoo.open();
        assertTrue(zoo.isOpen());
        verify(mockZooKeeper).isAvailable();
    }
    
    @Test
    public void testZooDoesNotOpenWhenKeeperNotAvailable() {
        when(mockZooKeeper.isAvailable()).thenReturn(false);
        Zoo zoo = new Zoo("Closed Zoo", mockZooKeeper);
        zoo.open();
        assertFalse(zoo.isOpen());
        verify(mockZooKeeper).isAvailable();
    }
    
    @Test
    public void testZooInitialStateIsCorrect() {
        Zoo zoo = new Zoo("Initial State Zoo", mockZooKeeper);
        assertEquals("Initial State Zoo", zoo.getName());
        assertEquals(0, zoo.getAnimals());
        assertFalse(zoo.isOpen());
    }
    
    @Test
    public void testCompleteZooOperationWithOneAnimalRemaining() {
        Zoo zoo = new Zoo("Complete Operation Zoo", mockZooKeeper);
        zoo.open();
        zoo.addAnimal();
        zoo.addAnimal();
        zoo.removeAnimal();
        assertEquals(1, zoo.getAnimals());
        assertTrue(zoo.isOpen());
        verify(mockZooKeeper, times(2)).canAddAnimal();
        verify(mockZooKeeper).canRemoveAnimal();
    }
}
