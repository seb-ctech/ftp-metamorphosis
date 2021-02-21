(ns metamorphosis.graphics.translation
    (:require 
        [quil.core :as q]
        [metamorphosis.graphics.glsl-parsing :as glsl]
        [metamorphosis.meta-ruleset.translation :as meta-t]
        ;TODO: Remove after testing
        [metamorphosis.meta-ruleset.formal-system-example-structure :as example]))

;This module contains translation functions that convert the abstract theorem structure 
;from the L-System to a graphical instruction set

;TODO: Adjust and play with these for interesting results

(def fs->quil {
    :unit [
        '(quil.core/ellipse 
            [
                (0 0 10 10)
                (0 0 10 20)
                (0 0 20 10)
                (0 0 20 5)
                (0 0 5 20)])
        '(quil.core/rect 
            [
                (0 0 10 10)
                (0 0 10 20)
                (0 0 20 10)
                (0 0 20 5)
                (0 0 5 20)])
        '(quil.core/triangle 
            [
                (-20 -20 0 0 30 30)
                (5 5 20 20 -20 -20)
                (1 5 2 3 3 4)
                (10 20 30 40 50 60)])]
    :transform [
        '(quil.core/translate 
            [
                (20 20)
                (40 10)
                (10 40)])
        '(quil.core/rotate 
            [
                (10)
                (20)
                (30)])
        '(quil.core/scale 
            [
                (0.5)
                (1.0)
                (1.2)
                (1.5)
                (2.0)])]
    :property [
        '(quil.core/fill [
            (0 200 30)
            (10 120 200)
            (0 10 0)
            (230 10 140)
        ])
        '(quil.core/stroke [
            (0 200 30)
            (10 120 200)
            (0 10 0)
            (230 10 140)
        ])
        '(quil.core/no-stroke)
        '(quil.core/no-fill)
        '(quil.core/stroke-weight [
            (0.2)
            (0.6)
            (1.0)
            (2.0)
        ])
    ]
})

(defn split-scopes [sequence]
    (loop [prefix []
           remaining sequence]
        (if (> (count remaining) 0) 
           (if (contains? (first remaining) :gen)
                {:pre prefix :scope (first remaining) :rest (rest remaining)}
                (recur (conj prefix (first remaining)) (rest remaining)))
            {:pre prefix})))

; FIXME: Probably too expensive! Slow on Gen 4...
; TODO: Implement glue
(defn recursive-translation [sequence]
    (let [{prefix :pre
           lower-scope :scope
           remaining :rest} (split-scopes sequence)]
        (let [prefix-translated (meta-t/fs-sequence->instructions prefix fs->quil)]
            (if lower-scope 
                (let [variation (conj (into (into ['(quil.core/push-matrix)] prefix-translated)
                                                (recursive-translation (:sequence lower-scope)) ) 
                                    '(quil.core/pop-matrix))]
                (if (> (count remaining) 0)
                    (let [temp (into variation (recursive-translation remaining))]
                        temp)
                    variation))
            prefix-translated))))
          
(defn test-translation []
    (recursive-translation (:sequence example/complex)))

;Not the right use-case for a macro, because of the way I pass the argument, which is not a form and needs to be evaluated to the data structure!
(defn make-quil
    "This is a function that transforms the formal system to valid quil instructions"
    [theorem]
    (let [instructions (recursive-translation (:sequence theorem))]
        (cons 'do instructions)))

(defn make-glsl 
    "This is a function that turns the formal-system into a glsl file"
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))