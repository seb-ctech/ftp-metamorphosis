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

(defn render-shader [shader]
    (q/background 0))

(defn draw-test-motif 
    [state]
    (q/background 0))

(defn render-generation [generation]
    (t/make-quil (t/build-quil-algorithm generation)))

(defn setup-sketch []
    {})

(defn update-graphics [state]
    state)

;renders the state that was updated
(defn draw-sketch [state]
    (if (= (:mode state) :glsl)
        (render-shader (:shader state))
        (if (contains? state :theorem)
            (render-generation (:theorem state))
            (q/background 0))))

; Programmatic creation of a quil sketch
(defn start-visualization 
    [size setup update-loop]
        (q/sketch 
            :size size 
            :display 2
            :features [:keep-on-top]
            :renderer :p2d
            :setup setup
            :update update-loop
            :draw draw-test-motif
            :middleware [m/fun-mode]))