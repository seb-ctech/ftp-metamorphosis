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

(def palette [
    '(18 23 23)
    '(30 46 51)
    '(145 161 95)
    '(82 176 104)
    '(38 232 154)
    '(255 180 66)
])

;TODO: Adjust glue to make composition fit screen better



(def fs->quil {
    :glue [
        (list
            (fn ([n]  
                    (q/scale (* (/ 1.0 n) 1.3)))
                ([n i]
                    (when (> i 1) (q/pop-matrix))
                    (q/push-matrix))))
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
                '(0.2)
                '(0.5)
                '(0.7)
                '(0.8)
                '(0.9)
                '(1.0)
                '(1.1)
                '(1.2)
                '(1.3)
                '(1.5)
                '(1.8)])
        (list
            (fn [x] 
                (q/translate (* 1 x) (* -1 x)))
            [
                '(10)
                '(20)
                '(40)
                '(60)
                '(100)
                '(140)
                '(200)
                '(300)])
        (list
            (fn [x] 
                (q/translate (* -1 x) (* 1 x)))
            [
                '(10)
                '(20)
                '(40)
                '(60)
                '(100)
                '(140)
                '(200)
                '(300)])
        (list
            (fn [x] 
                (q/translate (* 1 x) (* 1 x)))
            [
                '(10)
                '(20)
                '(40)
                '(60)
                '(100)
                '(140)
                '(200)
                '(300)])
        (list
            (fn [x] 
                (q/translate (* -1 x) (* -1 x)))
            [
                '(10)
                '(20)
                '(40)
                '(60)
                '(100)
                '(140)
                '(200)
                '(300)])
        (list
            (fn [a]
                (q/rotate a)) 
            [
                '(4)
                '(10)
                '(35)
                '(90)])
        (list
            (fn [a]
                (q/rotate (* -1.0 a))) 
            [
                '(4)
                '(10)
                '(35)
                '(90)])]
    :property [
        (list 
            (fn [r g b]
                (q/fill r g b)) 
            [
                '(18 23 23)
                '(30 46 51)
                '(145 161 95)
                '(82 176 104)
                '(38 232 154)
                '(255 180 66)])
        (list
            (fn [r g b] 
                (q/stroke r g b))  
            [
                '(18 23 23)
                '(30 46 51)
                '(145 161 95)
                '(82 176 104)
                '(38 232 154)
                '(255 180 66)])
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
                '(0.8)
                '(1.0)
                '(2.0)
                '(3.0)
                '(4.0)
                '(5.0)
                '(8.0)
                '(10.0)
                '(12.0)
                '(16.0)
                '(20.0)
                '(25.0)
                '(30.0)
                '(40.0)])]})

; FIXME: Probably too expensive! Slow on Gen 4...
(defn nested-graphics-composition 
    "A function that recursively translates entries and moves level down when it encounters lower-level structures"
    [sequence]
    (let [{prefix :pre
           lower-scope :scope
           remaining :rest} (fs/split-scopes sequence)]
        (let [prefix-translated (meta-t/fs-sequence->instructions prefix fs->quil)]
            (if lower-scope 
                (let [variation (conj 
                                    (into 
                                        (into ['(quil.core/push-matrix)] prefix-translated)
                                        (nested-graphics-composition (:sequence lower-scope)) ) 
                                    '(quil.core/pop-matrix))]
                (if (> (count remaining) 0)
                    (let [temp (into variation (nested-graphics-composition remaining))]
                        temp)
                    variation))
            prefix-translated))))

(defn make-quil
    "This is a function that transforms the formal system to a flattened valid quil instructions sequence"
    [theorem]
    ;(fs/print-theorem theorem)
    (let [instructions (nested-graphics-composition (:sequence theorem))]
        (cons 'do instructions)))

(defn make-glsl 
    "This is a function that turns the formal-system into a glsl file"
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))