# Metamorphosis

An Artistic installation that explores themes of reproduction (Like the one of DNA), motific evolution and the meaning of "Meta" and self-referential behavior in algorithmic art.

## Usage

Download and unzip this [archive](https://drive.google.com/file/d/10uyWhCL2uLZyRV_Dox1hj3GJaxmE-w-E/view?usp=sharing)

* Enter new Input sequence with "QWERT" keys
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

Copyright © 2021 Sebastian Wilhelm, Filmuniversität KONRAD WOLF

## Disclaimer

This Project makes use of [ohpauleez's](https://github.com/ohpauleez/clojure-leap) leap-motion wrapper for clojure.
But it is not implemented yet! 
