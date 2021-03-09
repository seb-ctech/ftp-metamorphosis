(ns metamorphosis.event-listener.input.core
    (:require [metamorphosis.meta-ruleset.formal-system.core :as f]
              [metamorphosis.event-listener.input.command-line :as cl]
              [metamorphosis.event-listener.input.keyboard :as key]))

;;=== PROTOTYPE OF ABSTRACT INPUT DATA-STRUCTURE ===

; What about forms at the same time?
; Inputs are made out of signals, that have an intensity and a duration.
; A Signal represent a discrete input form that can be distinguished from others


(def input-signals [:A :B :C :D :E])

(def test-input [
    {:signal :A :intensity 0.1 :duration 1/10}
    {:signal :B :intensity 0.1 :duration 3/10}
    {:signal :break :duration 2/10}
    {:signal :B :intensity 0.1 :duration 3/10}
])

; ==============================================

(def input-interval 4000)

(defn basic-keyboard 
    "A function that takes the input state a composes a sequence of abstract input signals based on keyboard input"
    [state]
    (let [pressed (get-in state [:key-pressed :key])
          released (get-in state [:key-released :key])
          signal (key/process-key pressed released)
          old-sequence (:input-sequence state)
          length (count old-sequence)
          sequence (if (= (count old-sequence) 0)
                       (conj old-sequence {:signal signal :duration 1})
                       (if (= (:signal (last old-sequence)) signal)
                           (update-in old-sequence [(dec length) :duration] inc)
                           (conj old-sequence {:signal signal :duration 1})))]
        (assoc state 
               :input-sequence sequence
               :key-pressed (if (= signal :break) nil (:key-pressed state))
               :key-released nil
               )))
 
(defn record-input 
    "A function that returns a future thread that waits for a timeout to resolve"
    []
    (clojure.core/future 
        (Thread/sleep input-interval)))

(defn process-input-sequence 
    "A function that goes over an assembled input sequence and processes it to a finalized version"
    [sequence]
    (let [length (apply + 
                        (map #(:duration %) 
                                sequence))]
    (map #(assoc % 
            :duration (/ (:duration %) length)) 
            sequence)))

(defn command-line 
    "A function that translates an input string to an input sequence"
    [string]
    (cl/string->input string))