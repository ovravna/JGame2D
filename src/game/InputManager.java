package game;

import java.util.ArrayList;
import java.util.List;

public abstract class InputManager {

    private static List<InputObject> inputObjects = new ArrayList<>();
    private static InputHandler input;


    public void addInputObject(InputObject inputObject) {
        inputObjects.add(inputObject);
        inputObject.setInputHandler(input);

    }


    public void setInput(InputHandler input) {
        this.input = input;

        for (InputObject inputObject : inputObjects) {
            inputObject.setInputHandler(this.input);
        }

        System.out.println("Origin "+this.input);
    }
}
