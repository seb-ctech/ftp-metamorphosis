(ns metamorphosis.event-listener
    (:require 
        [metamorphosis.event-listener.input :as in]
        [metamorphosis.meta-ruleset :as msys])
    (:gen-class))

(def test-input in/test-input)

(def input-listen-interval 500)

;TODO: Implement an asynchronous event-listener that can be used as wrapper to any input form (This Layer)
(defn listen-for-event []
    (Thread/sleep input-listen-interval)
    {:new-input? false :input (msys/build-random-axiom)})

(defn basic-keyboard 
    [](clojure.string/upper-case (read-line)))

(defn command-line [string]
    (in/command-line string))