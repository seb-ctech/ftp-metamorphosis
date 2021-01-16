(ns metamorphosis.meta-ruleset.formal-system)

;TODO: Define Alphabet as abstract structure that can be used to define mutating rules and graphic commands
;TODO: Implement function to create axiom from input-data-structure provided by the "event-listener" module

;=== PROTOTYPE OF AN ABSTRACT DATA STRUCTURE ===

; One generation contains the generation count for the scope a sequence of objects that define a class,
; that has a different meaning by the translation context it is used in and an index for the list of 
; commands of the same class. Classes can be of type: body, transform, property, amount.

; BODY: Draws some form of geometry; builds some sequence
; TRANSFORM: Changes the relation between Bodies in some form
; PROPERTY: Some property that the next Bodies will have
; AMOUNT: Some amount that either the next Body, Transform or Property will have. Default is "0"

(def example-theorem {:gen 0 
                      :sequence [{:class :transform :index 1}
                                 {:class :body :index 2}
                                 {:class :amount :index 1}
                                 {:class :transform :index 1}
                                 {:class :property :index 2}
                                 {:class :property :index 0}
                                 {:class :amount :index 2}]})


; The alphabet is made out of the different classes that commands can have

(def alphabet [:body :transform :property :amount])

(defn random-entry []
    {:class (get alphabet 
                 (rand-int (count alphabet)))
     :index (rand-int 5)})

(defn build-random-axiom 
    ([]
        (build-random-axiom (+ (rand-int 6) 3)))
    ([length]
        (loop [sequence []]
            (if (< (count sequence) length)
                (recur (conj sequence 
                            (random-entry)))
                {:gen 0 :sequence sequence}))))

(defn process-input [input] 
    (reduce #(conj %1 (str %2)) [] input))