(ns metamorphosis.graphics.translation
    (:require 
        [metamorphosis.graphics.glsl-parsing :as glsl]
        [metamorphosis.graphics.quil-instructions :as quil]))

;This module contains translation functions that convert the abstract theorem structure 
;from the L-System to a graphical instruction set

(defn build-quil-algorithm
    [meta-algorithm])

(defmacro make-quil
    "This is a macro that transforms a list of instructions to valid quil instructions"
    [algorithm])

(defn make-glsl 
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))