(ns aiezue.subtitle-corpus
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:gen-class))

(defn make-corpus
  "clean subtitles and make it a tiny text corpus"
  [& _]
  (let [files (next (file-seq (io/file "srt")))
        ;; using regexp to clean the subtitles
        time_stamp_scene_number_regexp #"\d+\n\d{2}:\d{2}:\d{2},\d{3} --> \d{2}:\d{2}:\d{2},\d{3}"
        html_tag_regexp #"</?.+?>"
        empty_line_regexp #"\n+"
        ;; functions to replace them
        replace_time_stamp_scene #(string/replace % time_stamp_scene_number_regexp "")
        replace_html_tag #(string/replace % html_tag_regexp "")
        replace_empty_line #(string/replace % empty_line_regexp "\n")]
    (for [file files
          ;; slurp it and then clean it one by one
          :let [content (->> (slurp (str file))
                             (replace_time_stamp_scene)
                             (replace_html_tag)
                             (replace_empty_line))]]
      (spit "subtitles_corpus.dat" content
            ;; append to ensure it will be a single file with all the contents
            :append true))))

(defn -main
  "put them up"
  [& _]
  (make-corpus))
