(ns metamorphosis.event-listener.core
    (:require 
        [clojure.core.async :as async]
        [metamorphosis.event-listener.input.core :as in]
        [metamorphosis.meta-ruleset.core :as msys])
    (:gen-class))

(def test-input in/test-input)

(def input-listen-interval 100)

;TODO: Implement an asynchronous event-listener that can be used as wrapper to any input form (This Layer)
;TODO: Remove msys after implementing

;This blocks the whole program!
(defn listen-for-event []
    (Thread/sleep input-listen-interval)
    {:new-input? false :input (msys/build-random-axiom)})

(defn basic-keyboard 
    [](clojure.string/upper-case (read-line)))

(defn command-line [string]
    (in/command-line string))