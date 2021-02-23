(ns metamorphosis.event-listener.core
    (:require 
        [clojure.core.async :as async]
        [metamorphosis.event-listener.input.core :as in]
        [metamorphosis.meta-ruleset.core :as msys])
    (:gen-class))

(def test-input in/test-input)

(def input-listen-interval 100)

(defn basic-keyboard [state]
    (println (:key-pressed state))
    state)

(defn command-line [string]
    (in/command-line string))

(defn process-input-sequence [sequence]
    (let [length (apply + 
                        (map #(:duration %) 
                              sequence))]
    (map #(assoc % :duration (/ (:duration %) length)) 
         sequence)))

;TODO: Implement an asynchronous event-listener that can be used as wrapper to any input form (This Layer)
;TODO: Remove msys after implementing

;This blocks the whole program!
(defn listen-for-event [state]
    (let [state (if (:done? state) (assoc state :done? false) state)]
        (if (:triggered? state)
            (let [state (assoc state :triggered? false :recording? true)]
                (println "Triggered!")
                (assoc state :event-recorder (in/record-input)))
            (if (:event-recorder state)
                (if (realized? (:event-recorder state))
                    (dissoc (assoc state :done? true :recording? false :input-sequence (:input-sequence state)) :event-recorder)
                    (basic-keyboard state))
                state))))

(defn get-event [state]
    {:new-input? (:done? state) :input (:input-sequence state)} )

