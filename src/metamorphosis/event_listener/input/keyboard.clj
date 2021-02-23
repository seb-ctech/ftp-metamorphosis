(ns metamorphosis.event-listener.input.keyboard
    (:require [clojure.core.async :as async]))

(defn process-key [key state]
    (let [{input-sequence :input-sequence} state]
        input-sequence))

(defn key-pressed [state key]
    (println "Pressed: " key)
    (assoc state 
        :triggered? (not (:recording? state)) 
        :key-pressed key))

(defn key-released [state key]
    (println "Release: " key)
    (if (:recording? state)
        (assoc state :key-released key)
        state))