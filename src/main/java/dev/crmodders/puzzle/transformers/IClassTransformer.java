package dev.crmodders.puzzle.transformers;

public interface IClassTransformer {

    byte[] transform(String name, String transformedName, byte[] basicClass);

}
