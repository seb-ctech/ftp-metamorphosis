(ns metamorphosis.graphics.translation
    (:require [metamorphosis.graphics.glsl-parsing :as glsl]))

;This module contains translation functions that convert the abstract theorem structure 
;from the L-System to a graphical instruction set

;TODO: Implement Formal System --> Quil translation


(defn quil-instructions [theorem]
    )

(defn make-quil
    "This is a function that transforms the formal system to valid quil instructions"
    [theorem]
    (let [instructions (quil-instructions theorem)]
    (eval (list (cons 'do instructions)))))

(defn make-glsl 
    "This is a function that turns the formal-system into a glsl file"
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))