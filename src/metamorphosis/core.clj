(ns metamorphosis.core
  (:require [metamorphosis.graphics :as gsys]
            [metamorphosis.event-listener :as esys]
            [metamorphosis.l-system :as lsys]))

(def resolution [1000 1000])
(def input-listen-interval 500)

;TODO: Implement event-listener System with abstract interface to whatever external system I choose that processes the input
(defn listen-for-event []
  (Thread/sleep input-listen-interval)
  (println "check for input")
  {:new-input? (< (rand) 0.2) :input (esys/record-input 1000)})

;TODO: Implement System that evolves current generation into more complex form
(defn evolve-next-generation [theorem]
  theorem)

;TODO: Implement System that transforms input to initial theorem of meta-algorithm system
(defn first-generation [input-string]
  input-string)

(defn metamorph-loop
  [state]
  (let [event (listen-for-event)
        restart? (:new-input? event)
        new-input (:input event)]
    (if restart?
      (assoc state :theorem (first-generation new-input))
      (if (contains? state :theorem)
        (assoc state :theorem (evolve-next-generation (:theorem state)))
        state))))
  
(defn -main []
  (gsys/start-visualization resolution metamorph-loop))