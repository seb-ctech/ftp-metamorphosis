(ns metamorphosis.event-listener.input.keyboard
    (:require [clojure.core.async :as async]))

(defn process-key 
    "Given the current pressed and released key gives a signal as output"
    [pressed released]
    (if (or (= pressed released) (nil? pressed))
        :break
        (case pressed
            :q :A
            :w :B
            :e :C
            :r :D
            :t :E
            :break)))

(defn key-pressed 
    "An event handler that gets passed to quil to detect, when a key is pressed"
    [state key]
    (assoc state 
        :triggered? (not (:recording? state)) 
        :key-pressed key))

(defn key-released [state key]
    "An event handler that gets passed to quil to detect, when a key is released"
    (if (:recording? state)
        (assoc state :key-released key)
        state))