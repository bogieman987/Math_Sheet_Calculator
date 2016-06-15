package com.ryanairth.mathsheetcalculator.Math;

import android.util.Log;

import com.ryanairth.mathsheetcalculator.Exceptions.InvalidMathOperatorException;

import java.util.List;

import static com.ryanairth.mathsheetcalculator.MainActivity.TAG;

/**
 * Created by Ryan Airth (Sweeney) on 22/05/2016.
 * Copyright information found in accompanying License.txt file.
 */
public class BlockEvaluator {
    // TODO - go through all the sequences recursively

    /*
        List of blocks used for the calculations
     */
    private List<Block> blocks;

    /**
     * Block calculator, object designed to specifically calculate the total of all the blocks entered
     * by the user
     *
     * @param blocks reference of the list that contains all the blocks
     */
    public BlockEvaluator(List<Block> blocks) {
        this.blocks = blocks;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Calculate and return the total of the maths equation entered by the user, used when all inputs
     * have been made and looking for the final value
     *
     * @return number representing the sum of the maths equation
     * @throws InvalidMathOperatorException exception thrown when an unexpected operator is found, cause would be unimplemented operators
     */
    public double calculateTotal() throws InvalidMathOperatorException {
        // If there aren't any elements in the blocks array, return zero
        if(blocks.size() <= 0) {
            Log.i(TAG, "Not enough blocks, returning zero");
            return 0.0;
        }

        return evaluate(new BlockSequence(blocks));
    }

    /**
     * Calculate and return the final evaluation of the maths equation entered by the user thus far
     *
     * @return number representing the sum of the maths equation
     * @throws InvalidMathOperatorException exception thrown when an unexpected operator is found, cause would be unimplemented operators
     */
    public double calculateCurrentTotal() throws InvalidMathOperatorException {
        // If there aren't any elements in the blocks array, return zero
        if(blocks.size() <= 0) {
            Log.i(TAG, "Not enough blocks, returning zero");
            return 0.0;
        }

        /*
            If there's only one or two elements return the current value as element 0 will be a number
            and element 1 will be a symbol/math operator
         */
        if(blocks.size() <= 2) {
            Log.i(TAG, "Not enough blocks, returning current value");
            return ((NumberBlock)blocks.get(0)).getValue();
        }

        return evaluate(new BlockSequence(blocks));
    }

    /*protected double evaluate() throws InvalidMathOperatorException {
        // Get the first number to start off
        double currentValue = 0.0;

        // We know there's at least a single element, so we know that blocks.get(0) will not fail
        currentValue = ((NumberBlock)blocks.get(0)).getValue();

        // Otherwise we're fine to carry on with our usual operations
        for(int i = 1; i < blocks.size(); i++) {
            // Test to see if final block is a symbol, if so, break, no need to continue
            if(i == blocks.size() - 1 && blocks.get(i) instanceof SymbolBlock) {
                break;
            }

            // Every time we loop we want the next thing to get to be the operator used
            MathOperator operator = ((SymbolBlock)blocks.get(i++)).getValue();
            // After we get the operator to use, we get the number that will be used to modify the current value
            double nextValue = ((NumberBlock)blocks.get(i)).getValue();

            // Then we operate on the previous number
            switch (operator) {
                case PLUS:
                    currentValue += nextValue;
                    break;
                case MINUS:
                    currentValue -= nextValue;
                    break;
                case MULTIPLY:
                    currentValue *= nextValue;
                    break;
                case DIVIDE:
                    currentValue /= nextValue;
                    break;
                case PERCENTAGE:
                    currentValue = (currentValue / nextValue) * 100;
                    break;
                case NONE:
                    // This should never occur, if it does, there's an issue when adding the operator
                    // to the block manager in any class that has this as an object
                default:
                    // Might not actually need to throw anything here, though, just hoping it would be useful
                    // perhaps it's an incorrect usage, not too used to throwing errors and exceptions
                    throw new InvalidMathOperatorException("Error calculating total, math operator is: "
                            + MathOperator.NONE);
            }
        }

        return currentValue;
    }*/

    /**
     *
     *
     * @param start
     * @return
     * @throws InvalidMathOperatorException
     */
    protected double evaluate(BlockSequence start) throws InvalidMathOperatorException {
        // Get the first number to start off
        double currentValue = 0.0;

        // If the first element is a block sequence containing other blocks, get value from that
        if(start.getBlocks().get(0) instanceof BlockSequence) {
            Log.i(TAG, "Entering block sequence");

            currentValue = start.getValue();
        } else {
            // We know there's at least a single element, so we know that blocks.get(0) will not fail
            currentValue = ((NumberBlock)start.getBlocks().get(0)).getValue();
        }

        // Otherwise we're fine to carry on with our usual operations
        for(int i = 1; i < start.getBlocks().size(); i++) {
            // Test to see if final block is a symbol, if so, break, no need to continue
            if(i == start.getBlocks().size() - 1 && start.getBlocks().get(i) instanceof SymbolBlock) {
                break;
            }

            // Every time we loop we want the next thing to get to be the operator used
            MathOperator operator = ((SymbolBlock)start.getBlocks().get(i++)).getValue();
            // After we get the operator to use, we get the number that will be used to modify the current value
            double nextValue;

            if(start.getBlocks().get(i) instanceof BlockSequence) {
                Log.i(TAG, "Entering block sequence");

                nextValue = (Double)start.getBlocks().get(i).getValue();
            } else {
                nextValue = ((NumberBlock)start.getBlocks().get(i)).getValue();
            }


            // Then we operate on the previous number
            switch (operator) {
                case PLUS:
                    currentValue += nextValue;
                    break;
                case MINUS:
                    currentValue -= nextValue;
                    break;
                case MULTIPLY:
                    currentValue *= nextValue;
                    break;
                case DIVIDE:
                    currentValue /= nextValue;
                    break;
                case PERCENTAGE:
                    currentValue = (currentValue / nextValue) * 100;
                    break;
                /*case LEFT_BRACKET:
                    currentValue *= nextValue;
                    break;*/
                case NONE:
                    /*
                        This should never occur, if it does, there's an issue when adding the operator
                        to the block manager in any class that has this as an object
                     */
                default:
                    // Might not actually need to throw anything here, though, just hoping it would be useful
                    // perhaps it's an incorrect usage, not too used to throwing errors and exceptions
                    throw new InvalidMathOperatorException("Error calculating total, math operator is: "
                            + MathOperator.NONE);
            }
        }

        return currentValue;
    }
}
