(ns simple-symbolic-regression-clojure.gp
  )

(defn random-token
  "Argument is a list of tokens, including functions to generate values if you like;
  every token is evaluated as it is returned, and they are sampled with uniform probability."
  [tokens]
  (if (empty? tokens)
    nil
    (eval (nth tokens (rand-int (count tokens))))
  ))

(defn uniform-crossover
  "Takes two collections, and returns a new collection containing items taken with
  equal probability from the two 'parents' at each location. The length of the
  crossover-product is the shorter of the two lengths."
  [mom dad]
  (map (fn [a b] (if (< 0.5 (rand)) a b)) mom dad)
  )

(defn uniform-mutation
  "Takes a collection, a vector of tokens (including functions), and a probability;
  resamples each position with i.i.d. probability, using the token list to replace them.
  The result will not change length."
  [mom tokens prob]
  (map
    (fn [i]
      (if (or (> (rand) prob) (empty? tokens))
        i
        (random-token tokens)
        ))
    mom)
  )

(defn one-point-crossover
  "Takes two collections, and returns a new collection containing items taken from the
  front of the first and the tail of the second, switching at a randomly-chosen breakpoint
  in each."
  [mom dad]
  (let [mom-cut (rand-int (count mom))
        dad-cut (rand-int (count dad))]
    (concat (take mom-cut mom) (drop dad-cut dad))
  ))


(defrecord Individual [script score])


(defn make-individual
  ([script]
   (make-individual script nil))
  ([script score]
   (->Individual script (atom score))))


(defn set-score [individual score]
  (swap! (:score individual) (fn [_] score)))


(defn get-score [individual]
  @(:score individual))


(defn random-script
  "Takes a collection of token-generators and a size, and samples the generators using
  random-token to produce a vector of `size` samples."
  [token-list size]
  (repeatedly size #(random-token token-list))
  )


(defn random-individual
  "takes a token list and size, and returns an un-scored Individual"
  [tokens size]
  (make-individual (random-script tokens size)))


(defn winners
  "takes a list of Individuals, scored, and returns
  a list containing all the Individuals with the lowest non-nil score;
  if no Individual has been scored, it returns an empty list"
  [individuals]
  (let [scored-ones (filter #(some? (get-score %)) individuals)
        sorted (sort-by get-score scored-ones)]
    (first (partition-by get-score sorted))
  ))