(ns metamorphosis.event-listener
    (:gen-class))

;TODO: Implement an abstract data-structure that get's processed to the beginning motif for the L-System (Layer 1)
;TODO: Process device-specific input to abstract data-structure (Layer 2)

(def input-interval 100)

(defn basic-keyboard 
    [](clojure.string/upper-case (read-line)))

(def a-input "a")

(defn build-input 
   [input-queue timer]
        (if (clojure.core/realized? timer)
            input-queue
            (do 
                (Thread/sleep input-interval)
                (build-input (conj input-queue a-input) timer))))

(defn record-input 
    ([time-frame]
        (record-input time-frame build-input))
    ([time-frame input]
    (input [] (clojure.core/future (Thread/sleep time-frame)))))