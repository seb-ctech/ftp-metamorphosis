(ns metamorphosis.graphics.translation
    (:require 
        [quil.core :as q]
        [metamorphosis.graphics.glsl-parsing :as glsl]
        [metamorphosis.meta-ruleset.translation :as meta-t]
        ;TODO: Remove after testing
        [metamorphosis.meta-ruleset.formal-system-example-structure :as example]))

;This module contains translation functions that convert the abstract theorem structure 
;from the L-System to a graphical instruction set


;TODO: Find visual translation made out of composable functions that is fitting for a beginning motif. Ideally 3D:
;      1. Every generation represents one layer of complexity contained in higher levels
;      2. One Layer of complexity is made out of an algorithm that contains the algorithms of the lower levels
;TODO: Adjust and play with these, for interesting results

(def fs->quil {
    :glue [
        (list
            (fn ([n]
                    (q/scale (/ 1.0 n)))
                ([n i]
                    (q/scale (/ 1.0 n)))))
    ]
    :unit [
        (list 
            (fn [a b c d] 
                (q/ellipse a b c d))
            [
                '(0 0 10 10)
                '(0 0 10 20)
                '(0 0 20 10)
                '(0 0 20 5)
                '(0 0 5 20)])
        (list
            (fn [a b c d]
                (q/rect a b c d))
            [
                '(0 0 10 10)
                '(0 0 10 20)
                '(0 0 20 10)
                '(0 0 20 5)
                '(0 0 5 20)])
        (list
            (fn [a b c d e f] 
                (q/triangle a b c d e f)) 
            [
                '(-20 -20 0 0 30 30)
                '(5 5 20 20 -20 -20)
                '(1 5 2 3 3 4)
                '(10 20 30 40 50 60)])]
    :transform [
        (list
            (fn [x y] 
                (q/translate x y))
            [
                '(20 20)
                '(40 10)
                '(10 40)])
        (list
            (fn [a]
                (q/rotate a)) 
            [
                '(10)
                '(20)
                '(30)])
        (list 
            (fn [a]
                (q/scale a))
            [
                '(0.5)
                '(1.0)
                '(1.2)
                '(1.5)
                '(2.0)])]
    :property [
        (list 
            (fn [r g b]
                (q/fill r g b)) 
            [
                '(0 200 30)
                '(10 120 200)
                '(0 10 0)
                '(230 10 140)])
        (list
            (fn [r g b] 
                (q/stroke r g b))  
            [
                '(0 200 30)
                '(10 120 200)
                '(0 10 0)
                '(230 10 140)])
        (list 
            (fn [] 
                (q/no-stroke)))
        (list 
            (fn [] 
                (q/no-fill)))
        (list 
            (fn [w] 
                (q/stroke-weight w)) 
            [
                '(0.2)
                '(0.6)
                '(1.0)
                '(2.0)])]})

(defn split-scopes 
    "A function that splits an input sequence into a prefix, the next lower-entry and the rest that comes after it"
    [sequence]
    (loop [prefix []
           remaining sequence]
        (if (> (count remaining) 0) 
           (if (contains? (first remaining) :gen)
                {:pre prefix :scope (first remaining) :rest (rest remaining)}
                (recur (conj prefix (first remaining)) (rest remaining)))
            {:pre prefix})))

; FIXME: Probably too expensive! Slow on Gen 4...
(defn recursive-translation 
    "A function that recursively translates entries and moves level down when it encounters lower-level structures"
    [sequence]
    (let [{prefix :pre
           lower-scope :scope
           remaining :rest} (split-scopes sequence)]
        (let [prefix-translated (meta-t/fs-sequence->instructions prefix fs->quil)]
            (if lower-scope 
                (let [variation (conj 
                                    (into 
                                        (into ['(quil.core/push-matrix)] prefix-translated)
                                        (recursive-translation (:sequence lower-scope)) ) 
                                    '(quil.core/pop-matrix))]
                (if (> (count remaining) 0)
                    (let [temp (into variation (recursive-translation remaining))]
                        temp)
                    variation))
            prefix-translated))))

(defn make-quil
    "This is a function that transforms the formal system to a flattened valid quil instructions sequence"
    [theorem]
    (println "Translating formal system to quil instructions...")
    (let [instructions (recursive-translation (:sequence theorem))]
        (cons 'do instructions)))

(defn make-glsl 
    "This is a function that turns the formal-system into a glsl file"
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))