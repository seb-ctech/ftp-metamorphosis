(ns metamorphosis.event-listener.input.keyboard
    (:require [clojure.core.async :as async]))

(defn process-key [pressed released]
    (if (or (= pressed released) (nil? pressed))
        :break
        (case pressed
            :q :A
            :w :B
            :e :C
            :r :D
            :t :E
            :break)))

(defn key-pressed [state key]
    (assoc state 
        :triggered? (not (:recording? state)) 
        :key-pressed key))

(defn key-released [state key]
    (if (:recording? state)
        (assoc state :key-released key)
        state))