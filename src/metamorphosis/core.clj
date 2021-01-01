(ns metamorphosis.core
  (:require [metamorphosis.graphics :as gsys]
            [metamorphosis.event-listener :as esys]
            [metamorphosis.l-system :as lsys]))

;TODO: Implement overall concurrent architecture
(defn metamorph-loop []
  (gsys/render (lsys/parse-input (esys/basic-keyboard)))
  (Thread/sleep 1000)
  (metamorph-loop))

;TODO: Implement event-listener System with abstract interface to whatever external system I choose that processes the input
(defn listen-for-event [])

;TODO: Implement System that evolves current generation into more complex form
(defn evolve-next-generation [])

;TODO: Implement Graphical system that translates the instruction system into graphic instructions
(defn render-current-generation [])


(defn -main [& args])