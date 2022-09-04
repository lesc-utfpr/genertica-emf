# Generation of Embedded Real-Time Code based on Aspects (GenERTiCA)

GenERTiCA is a tool created to support the Aspect-oriented Model Driven Engineering for Real-Time systems (AMoDE-RT).  
It uses a set of mapping rules scripts to transform a UML model into source code for a given target platform.  
For that, the Distributed Embedded Real-time Compact Specification (DERCS) model is used.   
Currently, GenERTiCA is capable of generating not only source code for software components, but also for hardware components using Hardware Description Languages (HDL).   
Furthermore, GenERTiCA identifies whether aspects of the Distributed Embedded Real-time Aspects Framework (DERAF) have been specified within UML models, and then weaves aspects' code into the generated code, realizing the handling of system's non-functional requirements using the chosen target platform.   

## Cloning
When cloning the repository, use `git clone --recursive [REPO_URL]` to also initialize the submodule.

If you have already cloned without doing this, the submodule can also be initialized after the fact by running  
`git submodule update --init`.

## Building
Run `./gradlew build` (or `gradlew.bat build` for the windows command prompt) in a terminal.  
The built distribution can be found in the `build/distributions/` directory.