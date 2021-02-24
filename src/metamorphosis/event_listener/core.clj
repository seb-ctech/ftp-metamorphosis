(ns metamorphosis.event-listener.core
    (:require 
        [clojure.core.async :as async]
        [metamorphosis.event-listener.input.core :as in])
    (:gen-class))

(def test-input in/test-input)

(def input-listen-interval 100)

(defn command-line [string]
    (in/command-line string))

(defn process-input-sequence [sequence]
    (let [length (apply + 
                        (map #(:duration %) 
                              sequence))]
    (map #(assoc % 
            :duration (/ (:duration %) length)
            :intensity 0.5) 
         sequence)))

(defn listen-for-event 
    "A function that handles an asynchronous timer and sets important variables for the handling of input"
    [state]
    (let [state (if (:done? state) 
                    (assoc state :done? false) 
                    state)]
        (if (:triggered? state)
            (let [state (assoc state :triggered? false :recording? true)]
                (println "Triggered!")
                (assoc state :event-recorder (in/record-input) :input-sequence []))
            (if (:event-recorder state)
                (if (realized? (:event-recorder state))
                    (dissoc 
                        (assoc state 
                            :done? true 
                            :recording? false 
                            :input-sequence (process-input-sequence (:input-sequence state))) 
                        :event-recorder)
                    (in/basic-keyboard state))
                state))))

(defn get-event 
    "A function that returns true, when a new input was produced and the new input"
    [state]
    ;(println (:input-sequence state))
    {:new-input? (:done? state) 
     :input (:input-sequence state)})

