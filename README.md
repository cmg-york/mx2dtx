# mx2dtx: an mxGraph to iStarDT-X Adapter for iStarDT-V

`mx2dtx` is a converter of iStarDT-V models build using mxGraph to the XML representation of iStar-DT called iStarDT-X.

iStar-DT both extends and subsets the iStar 2.0 modeling language to allow high-level decision-theoretic (DT) modeling of agents. iStarDT-V is a visual component of iStar-DT proposing specific shapes for building diagrammatic representations. iStarDT-X is an XML-based language for storing and sharing iStar-DT models.

`mx2dtx` converts iStarDT-V models built in mxGraph-compatible tools and saved in an the native mxGraph XML format into iStarDT-X files for usage by other tools within the iStar-DT ecosystem.

## Building iStarDT-V models in draw.io

iStarDT-V diagrams can be created in [draw.io](https://app.diagrams.net/), [mxGraph](https://jgraph.github.io/mxgraph/) front-end, using a library of shapes specifically created for the purpose. In draw.io (we have tried version 14.1.8) go to `File --> Open Library` and select the [iStarDT-V.xml](https://github.com/cmg-york/mx2dtx/blob/main/src/main/resources/iStarDT-V.xml) file. Continue building the model exclusively using the library, and save the result in an uncompressed format. In version 14.1.8 of draw.io go to `File --> Properties` and uncheck `Compressed`. 
## How-to Page
Detailed HOW-TO page can be found [here](doc/HOWTO.md)
## Installing and using mx2dtx

- Ensure you have maven, Java and git installed in your system. We have tested with Maven version 3.9.9 and Java 21.0.4.
- Clone the repository: `git clone https://github.com/cmg-york/mx2dtx`
- `cd mx2dtx`
- `mvn compile` to compile
- Run with `mvn exec:java -Dexec.args="-f [drawio input file] -o [iStarDT-X output file]"` to convert your diagram. The `-o` option is optional - if omitted, the output will be printed to standard output.
- Use `-h` option to see usage information and available options. **NOTE:** if you are using Windows PowerShell, add `--%` between `exec:java` and `-Dexec`
- If you want to run from native java call: `java -cp target/classes cmg.gReason.outputs.istardtx.mx2dtx -f [drawio input file] -o [iStarDT-X output file]`
- Example models can be found in `src/main/resources` and `src/test/resources`

## Usage Notes

- Custom numeric formulae for qualities are supported only in iStarDT-X at this point. Prepare the formula in a text editor and paste within the `dtxFormula` attribute of qualities. Otherwise, ensure a tree-like hierarchy of qualities. Satisfaction level of a quality is the linear combination of the satisfaction levels of the origins multiplied by the contribution labels.
- To reference goals, tasks and qualities in formulae or lists, use camel-case representation of the label in the corresponding element. For example goal `Have Meeting Scheduled` is referenced as `haveMeetingScheduled`.
- It is important that all links/connectors properly connect directly on the whole shapes. This can be achieved by dragging the end-point of the link on the target shape and drop it when the shape acquires a blue outline.

## Related Tool

iStarDT-X documents can be validated, deserialized, and translated to a formal specification through the [dtx2X](https://github.com/cmg-yorku/dtx2X) tool.

## Contact

- For bug reports and queries please contact [liaskos@yorku.ca](liaskos@yorku.ca).  
