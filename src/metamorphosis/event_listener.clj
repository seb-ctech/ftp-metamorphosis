(ns metamorphosis.event-listener
    (:require 
        [metamorphosis.event-listener.input :as in]
        [metamorphosis.meta-ruleset :as msys])
    (:gen-class))

;TODO: Implement an asynchronous event-listener that can be used as wrapper to any input form (This Layer)
;TODO: Implement an "abstract data-structure" that abstracts the device specific input and can be processed into an axiom for the formal system (Layer 1)
;TODO: Process device-specific input to abstract data-structure (Layer 2)

(def input-listen-interval 500)

;TODO: Implement event-listener System with abstract interface to whatever external system I choose that processes the input
(defn listen-for-event []
    (Thread/sleep input-listen-interval)
    {:new-input? false :input (msys/build-random-axiom)})

(defn basic-keyboard 
    [](clojure.string/upper-case (read-line)))