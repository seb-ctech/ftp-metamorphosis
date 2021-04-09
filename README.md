# Metamorphosis

This is my first semester project for "M.A. Creative Technologies" at the Filmuniversität KONRAD WOLF.
It is a framework/interactive program that explores themes of reproduction, motivic evolution and the meaning of "meta" and self-referential behavior in algorithmic art.

## Usage

Download and unzip this [archive](https://drive.google.com/file/d/1mlfVL8iEI_4t0gcK-hLLWkTH0OeMvuXJ/view?usp=sharing)
Run the ".jar" as executable or via "java -jar <filename>" within the folder. 
*The program needs the "resources" folder to operate correctly.*

* Enter a new Input sequence with **QWERT** keys (You have 4 seconds)
* Wait for the system to progress through the generations
* Review the results in the "resources/results" folder
* You can interrupt an ongoing generation by providing a new input, however this might take a while from GEN 3 upwards

### With Clojure and Leiningen:

From the REPL - run 

1. `(require 'metamorphosis.core :reload-all)`.
    - Then `(-main)`.

2. `(use 'metamorphosis.core :reload-all true)`

3. `(clojure.main/main "-m" 'metamorphosis.core)`

## License

Copyright © 2021 Sebastian Wilhelm

## Disclaimer

This Project makes use of [ohpauleez's](https://github.com/ohpauleez/clojure-leap) leap-motion wrapper for clojure.
But it is not implemented yet! 
