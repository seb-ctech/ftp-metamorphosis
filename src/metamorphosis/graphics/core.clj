(ns metamorphosis.graphics.core
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [metamorphosis.meta-ruleset.formal-system.core :as fs]
              [metamorphosis.graphics.translation :as t]
              [metamorphosis.event-listener.input.keyboard :as key])
    (:gen-class))

; ====== GLSL STUFF for potential later implementations ========

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

;========================================================

(defn render-generation 
    "Function that evaluates the instruction set to render the current generation"
    [instructions]
    (q/translate (/ (q/width) 2) (/ (q/height) 2))
    (q/no-stroke)(q/fill 0 0 41)
    (eval instructions))

(defn setup-sketch 
    "A function that is called as part of the initialization process and sets important graphical information"
    []
    {})


(defn handle-instructions 
    "A function that manages the translation of instructions and caches already translated instructions"
    [state]
    (let [instructions 
        (if (contains? state :g-instructions)
            (if (not= (:last-gen state) (get-in state [:theorem :gen]))
                (let [new (t/make-quil (:theorem state))] new)
                (:g-instructions state))
            (t/make-quil (:theorem state)))]
    (assoc (assoc state :g-instructions instructions)
            :last-gen (get-in state [:theorem :gen]))))

(defn update-graphics 
    "A wrapper function that contains the main graphical updates."
    [state]
    (if (nil? (:theorem state))
        state
        (handle-instructions state)))

(defn draw-sketch 
    "Main function that renders a frame"
    [state]
    (q/background 0)
    (if (= (:mode state) :glsl)
        (render-shader (:shader state))
        (when (contains? state :theorem)
            (render-generation (:g-instructions state)))))

(defn start-visualization 
    "A function that builds a graphical container as quil sketch and passes in the most important high-level functions"
    [size setup update-loop]
        (q/sketch 
            :size size 
            :display 2
            :features [:keep-on-top]
            :renderer :p2d
            :setup setup
            :update update-loop
            :draw draw-sketch
            :key-pressed key/key-pressed
            :key-released key/key-released
            :middleware [m/fun-mode]))