(ns metamorphosis.core
  (:require [metamorphosis.graphics :as gsys]
            [metamorphosis.event-listener :as esys]
            [metamorphosis.l-system :as lsys])
  (:gen-class))

(def resolution [500 500])
(def input-listen-interval 500)

;TODO: Implement event-listener System with abstract interface to whatever external system I choose that processes the input
(defn listen-for-event []
  (Thread/sleep input-listen-interval)
  {:new-input? (< (rand) 0.02) :input (lsys/build-random-axiom)})

;TODO: Implement System that evolves current generation into more complex form
(defn evolve-next-generation [theorem]
  theorem)

;TODO: Implement System that transforms input to initial theorem of meta-algorithm system
(defn first-generation [input-string]
  input-string)

;TODO: Implement a timer after which the next generation get's evolved
;TODO: Implement gsys/reload-shader
(defn metamorph-loop
  [state]
  (let [event (listen-for-event)
        restart? (:new-input? event)
        new-input (:input event)]
    (if restart?
      (assoc state :theorem (first-generation new-input))
      (if (contains? state :theorem)
        (do
          (println (:theorem state))
          (assoc state :theorem (evolve-next-generation (:theorem state))))
        state))))
  
(defn -main [& args]
  (println args)
  (gsys/start-visualization resolution metamorph-loop))