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
            (fn ([n])
                ([n i])))
    ]
    :unit [
        (list 
            (fn [w h] 
                (q/ellipse 0 0 (* w (/ (q/width) 2)) (* h (/ (q/height) 2))))
            [
                '(0.5 0.5)
                '(0.5 1.0)
                '(1.0 0.5)
                '(1.0 0.2)
                '(0.2 1.0)])
        (list
            (fn [w h]
                (q/rect 0 0 (* w (/ (q/width) 2)) (* h (/ (q/height) 2))))
            [
                '(0.5 0.5)
                '(0.5 1.0)
                '(1.0 0.5)
                '(1.0 0.2)
                '(0.2 1.0)])
        (list
            (fn [a b c d e f]
                (let [w (/ (q/width) 2)
                      h (/ (q/height) 2)]
                (q/triangle 
                    (* a w)
                    (* b h)
                    (* c w)
                    (* d h)
                    (* e w)
                    (* f h)))) 
            [
                '(-0.2 0 0.05 0.4 0.2 0.1)
                '(-0.1 0.05 0.05 0.1 0.05 -0.3)
                '(-0.3 0.05 0.0 -0.2 0.3 0.05)
                '(-0.2 0.1 0.0 -0.2 0.2 0.1)])]
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

(defn compose-scope 
    "A function that recursively translates entries and moves level down when it encounters lower-level structures"
    [sequence]
    (let [nested? (fs/has-sub-seq? sequence)]
        (if nested?
            (let [copies (fs/count-copies sequence)
                  scale (if (> copies 0)(/ 1.0 copies) 1.0)]
                (loop [{prefix :pre
                        lower-scope :scope
                        remaining :rest} (fs/split-scopes sequence)
                        scoped-instructions ['(quil.core/push-matrix) (list 'quil.core/scale scale)]]
                        (let [prefixed-sub-seq (into (into scoped-instructions prefix) (compose-scope lower-scope))]
                            (if remaining
                                (recur (fs/split-scopes remaining) prefixed-sub-seq)
                                (conj prefixed-sub-seq '(quil.core/pop-matrix)))))
            (meta-t/fs-sequence->instructions sequence fs->quil)))))

(defn make-quil
    "This is a function that transforms the formal system to a flattened valid quil instructions sequence"
    [theorem]
    (fs/print-theorem theorem)
    (let [instructions (compose-scope (:sequence theorem))]
        (println instructions)
        (cons 'do instructions)))

(defn make-glsl 
    "This is a function that turns the formal-system into a glsl file"
    [glsl-structure]
    (glsl/write-file "meta" (glsl/assemble-glsl)))