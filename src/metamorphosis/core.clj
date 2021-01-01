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
  {:new-input (< (rand) 0.08) :motif ["A" "B" "C"]})

;TODO: Implement System that evolves current generation into more complex form
(defn evolve-next-generation [state]
  state)

;TODO: Implement overall concurrent architecture
(defn metamorph-loop
  []
  (let [event (listen-for-event)
        restart? (:new-input event)
        initial (:motif event)]
    (println event)
    (if restart?
      (do
        (gsys/start-visualization resolution initial evolve-next-generation)
        (metamorph-loop))
      (metamorph-loop))))
  


(defn -main [& args])

(defn open-close-sketch 
  []
  (let [sketch (gsys/start-visualization resolution [] evolve-next-generation)]
    (println sketch (type sketch))
    (Thread/sleep 200)
    (quil.core/with-sketch sketch quil.core/exit)))