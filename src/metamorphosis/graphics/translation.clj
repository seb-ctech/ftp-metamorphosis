(ns metamorphosis.graphics.translation
    (:require 
        [quil.core :as q]
        [metamorphosis.graphics.glsl-parsing :as glsl]
        [metamorphosis.meta-ruleset.translation :as meta-t]
        [metamorphosis.meta-ruleset.formal-system.core :as fs]
        ;TODO: Remove after testing
        [metamorphosis.meta-ruleset.formal-system.examples :as example]))

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
                    (q/scale (* (/ 1.0 n) 1.5)))
                ([n i]
                    )))
    ]
    :unit [
        (list 
            (fn [a b c d] 
                (q/ellipse a b c d))
            [
                '(0 0 100 100)
                '(0 0 100 200)
                '(0 0 200 100)
                '(0 0 200 50)
                '(0 0 50 200)])
        (list
            (fn [a b c d]
                (q/rect a b c d))
            [
                '(0 0 100 100)
                '(0 0 100 200)
                '(0 0 200 100)
                '(0 0 200 50)
                '(0 0 50 200)])
        (list
            (fn [a b c d e f] 
                (q/triangle a b c d e f)) 
            [
                '(-20 0 5 -40 20 10)
                '(-10 5 5 10 5 -30)
                '(-30 5 0 -20 30 5)
                '(-20 10 0 -20 20 10)])]
    :transform [
        (list 
            (fn [a]
                (q/scale a))
            [
                '(0.5)
                '(1.0)
                '(1.2)
                '(1.5)
                '(2.0)])
        (list
            (fn [x] 
                (q/translate (* 1 x) (* -1 x)))
            [
                '(100)
                '(200)
                '(400)])
        (list
            (fn [x] 
                (q/translate (* -1 x) (* 1 x)))
            [
                '(100)
                '(200)
                '(400)])
        (list
            (fn [x] 
                (q/translate (* 1 x) (* 1 x)))
            [
                '(100)
                '(200)
                '(400)])
        (list
            (fn [x] 
                (q/translate (* -1 x) (* -1 x)))
            [
                '(100)
                '(200)
                '(400)])
        (list
            (fn [a]
                (q/rotate a)) 
            [
                '(4)
                '(10)
                '(35)
                '(90)])
        ]
    :property [
        (list 
            (fn [r g b]
                (q/fill r g b)) 
            [
                '(0 0 41)
                '(30 46 51)
                '(145 161 95)
                '(82 176 104)
                '(232 38 87)])
        (list
            (fn [r g b] 
                (q/stroke r g b))  
            [
                '(0 0 41)
                '(30 46 51)
                '(145 161 95)
                '(82 176 104)
                '(232 38 87)])
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
                '(0.5)
                '(1.0)
                '(2.0)
                '(4.0)
                '(10.0)])]})

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
    (fs/print-theorem theorem)
    (println "Translating formal system to quil instructions...")
    (let [instructions (recursive-translation (:sequence theorem))]
        (cons 'do instructions)))

(defn make-glsl 
    "This is a function that turns the formal-system into a glsl file"
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))