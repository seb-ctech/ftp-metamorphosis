(ns metamorphosis.event-listener.input)

;;=== PROTOTYPE OF ABSTRACT INPUT DATA-STRUCTURE ===

(def input-interval 2000)
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