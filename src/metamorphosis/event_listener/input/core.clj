(ns metamorphosis.event-listener.input.core
    (:require [metamorphosis.meta-ruleset.formal-system :as f]
              [metamorphosis.event-listener.input.command-line :as cl]))

;;=== PROTOTYPE OF ABSTRACT INPUT DATA-STRUCTURE ===

; What about forms at the same time?
; Inputs are made out of signals, that have an intensity and a duration.
; A Signal represent a discrete input form that can be distinguished from others


(def input-signals [:A :B :C])

(def test-input [
    {:signal :A :intensity 0.1 :duration 1/10}
    {:signal :B :intensity 0.1 :duration 3/10}
    {:signal :break :duration 2/10}
    {:signal :B :intensity 0.1 :duration 3/10}
])

; ==============================================

(def input-interval 2000)
(def a-input "a")

(defn build-input 
    [input-queue timer]
         (if (clojure.core/realized? timer)
             input-queue
             (do 
                 (Thread/sleep input-interval)
                 (build-input (conj input-queue a-input) timer))))
 
(defn record-input []
    (clojure.core/future (Thread/sleep input-interval) (println "Finished Recording")))

(defn command-line [string]
    (cl/string->input string))