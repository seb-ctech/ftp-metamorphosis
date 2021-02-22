(ns metamorphosis.graphics.core
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

(defn render-generation [instructions]
    (eval instructions))

(defn setup-sketch []
    {})

(defn update-graphics [state]
    (println "Rendering gen: " (get-in state [:theorem :gen]))
    (let [instructions 
            (if
                (contains? state :g-instructions)
                (if 
                    (not= (:last-gen state) (get-in state [:theorem :gen]))
                    (t/make-quil (:theorem state))
                    (:g-instructions state))
                (t/make-quil (:theorem state)))]
        (assoc (assoc state :g-instructions instructions)
                :last-gen (get-in state [:theorem :gen]))))

;renders the state that was updated
(defn draw-sketch [state]
    (q/background 0)
    (if (= (:mode state) :glsl)
        (render-shader (:shader state))
        (when (contains? state :theorem)
            (render-generation (:g-instructions state)))))

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
            :draw draw-sketch
            :middleware [m/fun-mode]))