import nltk
import spacy
import stanza

# =============================================================================
print("WITH NLTK")
nltk.download('punkt')

print("\n===Word tokenization===")
tokenized = nltk.tokenize.word_tokenize("I am happy. Mr. Wang is happy too")
print(tokenized)

print("\n===Sentence segmentation===")
split1 = nltk.tokenize.sent_tokenize("I am happy. Mr. Wang is happy too")
split2 = nltk.tokenize.sent_tokenize("I am happy, Mr. Wang is happy too")

print(split1)
print(split2)

# ======================================================================
print("\n\nWITH SPACY")

nlp = spacy.load("en_core_web_sm", exclude=["tok2vec", 'tagger','parser','ner','attribute_ruler','lemmatizer'])
nlp.add_pipe("sentencizer")

#Word segmentation
print("\n===Word tokenization===")
[[print(word.text) for word in doc]
 for doc in nlp.pipe(['today is monday. it is the first day.', 'soup is yammy. pizza sucks.'])]

#Sentence segmentation
print("\n===Sentence segmentation===")
[[print(sent.text) for sent in doc.sents]
 for doc in nlp.pipe(['today is monday. it is the first day.', 'soup is yammy. pizza sucks.'])]

# ======================================================================
print("\n\nWITH STANZA")

#stanza.download('en')
nlp = stanza.Pipeline(lang='en', processors='tokenize')

sentences = 'I am very very happy. The weather is very good.'

#Word segmentation
print("\n===Word tokenization===")
[[print(token.text) for token in sentence.tokens] for sentence in nlp(sentences).sentences]

#Sentence segmentation
print("\n===Sentence segmentation===")
[print(sentence.text) for sentence in nlp(sentences).sentences]

