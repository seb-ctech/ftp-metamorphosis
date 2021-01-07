(ns metamorphosis.graphics
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [metamorphosis.graphics.translation :as t])
    (:gen-class))

;TODO: Find visual translation made out of composable functions that is fitting for a beginning motif. Ideally 3D:
;      1. Every generation represents one layer of complexity contained in higher levels
;      2. One Layer of complexity is made out of an algorithm that contains the algorithms of the lower levels

(def fragment-shader "test.frag")

(defn reload-shader 
    ([] (reload-shader {}))
    ([state]
        (assoc state :shader (q/load-shader fragment-shader))))

(defn test-glsl [shader]
   (when (q/loaded? shader)
    (q/shader shader)
    (q/rect 0 0 (q/width)(q/height))))

(defn setup-sketch []
   {})

(defn draw-test-motif 
    [state]
    (q/background 0))

(defn render-generation [generation]
    (t/make-quil (t/build-quil-algorithm generation)))

;renders the state that was updated
(defn draw-sketch [state]
    (if (contains? state :theorem)
        (render-generation (:theorem state))
        (q/background 0)))

; Programmatic creation of a quil sketch
(defn start-visualization [size update-loop]
    (q/sketch 
        :size size 
        :renderer :p2d
        :setup setup-sketch
        :update update-loop
        :draw draw-test-motif
        :middleware [m/fun-mode]))