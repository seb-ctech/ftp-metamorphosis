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
    (q/push-style)
    (eval instructions)
    (q/pop-style))

(defn setup-sketch 
    "A function that is called as part of the initialization process and sets important graphical information"
    []
    {})

(defn handle-instructions 
    "A function that manages the translation of instructions and caches already translated instructions"
    [state]
    (let [instructions 
        (if (:recording? state)
            nil
            (if (contains? state :g-instructions)
                (if (not= (:last-gen state) (get-in state [:theorem :gen]))
                    (let [new (t/make-quil (:theorem state))] new)
                    (:g-instructions state))
                (t/make-quil (:theorem state))))]
    (assoc state 
        :g-instructions (if (:triggered? state) nil instructions)
        :last-gen (get-in state [:theorem :gen]))))

(defn update-graphics 
    "A wrapper function that contains the main graphical updates."
    [state]
    (if (nil? (:theorem state))
        state
        (handle-instructions state)))

(defn generation-indicator [state]
    (let [{max :max-gen
           theorem :theorem} state
           current (:gen theorem)
           spacing 20
           padding 10
           colors t/palette]
        (q/push-matrix)
        (q/translate (- (/ (q/width) 2) (* spacing (inc max) 0.5)) (- (q/height) (* padding 2) 20))
        (q/fill 0 0 0)
        (q/rect 0 0 (+ (* spacing (inc max)) (* padding 2)) (+ 5 (* padding 2)))
        (q/push-matrix)
        (q/translate padding padding)
        (doseq [x (range (inc max))]
            (q/push-matrix)
            (q/translate (* x spacing) 0)
            (cond (< x current)
                    (do
                    (q/fill 220 230 230)
                    (q/rect 0 0 (* spacing 0.95) 4))
                  (= x current)
                    (do
                    (apply q/fill (colors 5))
                    (q/rect 0 0 (* spacing 0.95) 4))
                  (> x current)
                    (do 
                    (q/fill 120 140 145)
                    (q/rect 0 0 (* spacing 0.95) 4)))
            (q/pop-matrix))
        (q/pop-matrix)
        (q/pop-matrix)))
            

(defn input-recording [state]
    (let [{rec :recording?
           seq :input-sequence} state
           colors t/palette
           margin 100
           padding 4
           seq-width (- (q/width) (* margin 2))
           seq-height 25]
    (when rec
        (apply q/fill (conj (into (vector) (colors 1)) 40))
        (q/rect 0 0 (q/width) (q/height))
        (q/push-matrix)
        (q/translate margin (/ (q/height) 2))
        (q/push-matrix)
        (doseq [input seq]
            (let [duration (float (/ (:duration input) (apply + (map :duration seq))))
                  width (* seq-width duration)
                  signal (:signal input)]
                (q/no-fill)
                (q/stroke 255)
                (q/stroke-weight 2)
                (if (= signal :break)
                    (do 
                        (q/push-matrix)
                        (q/translate 0 (/ seq-height 2))
                        (q/line 0 0 width 0)
                        (q/pop-matrix))
                    (do
                        (q/rect 0 0 width seq-height)
                        (q/push-matrix)
                        (q/translate (/ width 2) (/ seq-height 2))
                        (q/fill 255)
                        (q/no-stroke)
                        (q/text-align :center :center)
                        (q/text (name signal) 0 0 )
                        (q/pop-matrix)))
                (q/translate (+ width padding) 0)))
        (q/pop-matrix)
        (q/pop-matrix))))

(defn draw-user-feedback [state]
    (input-recording state)
    (when (:theorem state) (generation-indicator state)))

(defn draw-sketch 
    "Main function that renders a frame"
    [state]
    (q/background 0)
    (q/no-stroke)
    (q/fill 18 23 23)
    (if (= (:mode state) :glsl)
        (render-shader (:shader state))
        (when (contains? state :theorem)
            (render-generation (:g-instructions state))))
    (draw-user-feedback state))

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