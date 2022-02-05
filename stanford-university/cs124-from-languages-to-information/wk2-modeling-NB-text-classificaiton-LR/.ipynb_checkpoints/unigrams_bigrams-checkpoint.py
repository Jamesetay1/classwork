import random
import re
import numpy as np
from string import punctuation
from os.path import exists
from nltk.tokenize import sent_tokenize, word_tokenize



#Take an file to turn into into unigrams and bigrams
#Optionally take a test set to compare against and get perplexity
def compute_unigrams_bigrams(corpus, sentences_to_generate=0, test_set=None):

    #Tokenize sentences and words with nltk, remove punctuation, add beginning and ending sentence markers
    tokenized_text = preprocess_text(corpus)
    
    #Get unigrams
    N, unigram_dict, bigram_dict, bigram_mapping = get_ngrams(tokenized_text)

    sorted_unigram = sort_dict_by_second_tuple_value(unigram_dict)
    sorted_bigram = sort_dict_by_second_tuple_value(bigram_dict)
    
    print(f'\nnumber of words N:{N}')
    print('\nUNIGRAMS')
    print(f'unigram dict: {sorted_unigram}')
    print('\nBIGRAMS')
    print(f'bigram dict: {sorted_bigram}')
    print('\nBIGRAM MAPPING')
    pretty_print_bigram_mapping(bigram_mapping)
    
    #Generate a given amount of sentences
    if sentences_to_generate:
        print(f'\n\nGENERATING {sentences_to_generate} SENTENCE(S) BASED ON THE CORPUS')
        generated_sentences = generate_sentences(bigram_mapping, sentences_to_generate)
        print(generated_sentences)
        
    if test_set:
        print(f"\n\nCOMPUTING PERPLEXITY OF TEST SET: \"{test_set}\"")
        perplexity = compute_perplexity(N, unigram_dict, bigram_mapping, test_set)
        print(f'PERPLEXITY IS: {perplexity}')

#===========================================================================================================

def preprocess_text(corpus):
    print('\nPREPROCESSING TEXT')
    
    if exists(corpus):
        with open(corpus) as f:
            contents = f.read() 
    else:
        print('NOTICE: file not found, defaulting to input as string')
        contents = corpus
        
    tokenized_list = [word_tokenize(t) for t in sent_tokenize(contents)]     
        
    #add starting and ending line markers, remove punctuation
    for s in tokenized_list:
        for t in s:
            if (any(p in t for p in "!\"#$%&'()*+, -)./:;<=>?@[\]^_`’{|}~")):
                s.remove(t)
                
        s.insert(0, '<s>')
        s.insert(len(s), '</s>')
        
    return tokenized_list

def get_ngrams(tokenized_list):
    #Get total count of tokens and sentences
    N = 0
    S = 0
    for l in tokenized_list:
        N += len(l)
        S += 1
        
    bigram_N = N-S
    
    #Create unigram dictionary of {word: (number, proportion)
    unigram_dict = {}
    bigram_dict = {}
    bigram_mapping_dict = {}
    
    for s in tokenized_list:
        prev_word = None
        for token in s:    
            #Create unigram dictionary
            if token not in unigram_dict: unigram_dict[token] = (1, 1/N) 
            else: 
                uni_count = unigram_dict.get(token)[0] + 1
                unigram_dict[token] = (uni_count, uni_count/N)
            
            #Create bigram dictionary
            if prev_word: 
                bigram = (prev_word, token)
                if bigram not in bigram_dict: bigram_dict[bigram] = (1, 1/bigram_N)
                else: 
                    bi_count = bigram_dict.get(bigram)[0] + 1
                    bigram_dict[bigram] = (bi_count, bi_count/bigram_N)
               
                #Also create a one to many mapping for sentence generating.
                
                #If prev word is not in the mapping dict add it, and also add the token with value 1
                if prev_word not in bigram_mapping_dict: 
                    bigram_mapping_dict[prev_word] = {}
                    bigram_mapping_dict.get(prev_word)[token] = 1
                    
                else: #If it already is, find it and check if the token is already there. if not add if so increment
                    if token in bigram_mapping_dict.get(prev_word):
                        word_count = bigram_mapping_dict.get(prev_word).get(token)
                        bigram_mapping_dict.get(prev_word)[token] = word_count + 1
                        
                    else: bigram_mapping_dict.get(prev_word)[token] = 1
   
            prev_word = token    
    
    return N, unigram_dict, bigram_dict, bigram_mapping_dict

#Sort dictionay by the value of the second tuple
def sort_dict_by_second_tuple_value(dictionary):
    return dict(reversed(sorted(dictionary.items(), key=lambda item: item[1])))

#Generate sentences given a many to one bigram mapping and a number of sentences to create
def generate_sentences(bigram_mapping, num):
    
    sentences_list = []
    
    for i in range(num):
        current_sentence = ""
        current_word = '<s>'
        current_sentence = current_word
        while current_word != '</s>':
            if current_word in bigram_mapping:
                current_word = random.choice(list(bigram_mapping.get(current_word)))
                
            else:
                current_word = '</s>'
                
            current_sentence = current_sentence + ' ' + current_word
                 
        current_sentence = current_sentence + '.'
        sentences_list.append(current_sentence)
        
    return sentences_list

def compute_perplexity(num_words, unigram_dict, bigram_mapping, test_set):
    #Clean and tokenize test set
    test_set_tokenized = preprocess_text(test_set)
    
    #Just do the first sentence for now
    test_set_0 = test_set_tokenized[0]
    
    #Compute bigram probabilities, #falling back to unigram probability if bigram doesn't exist
    bigram_probabilities = np.array([])
    N = len(test_set_0)
    
    #If I come back to it, this should be changed a bit so that it actually starts with <s> <s> for the start of the sentence? 
    #Not sure if I should have N (number of words) or N-1 (i.e. 15 words and 14 probabilities)
    prev_token = '<s>'
    for i in range(1, len(test_set_0)):
        token = test_set_0[i]  
        if token not in bigram_mapping or token not in bigram_mapping.get(prev_token):
            if token in unigram_dict:
                bigram_probabilities = np.append(bigram_probabilities, unigram_dict.get(token)[1])
            else:
                bigram_probabilities = np.append(bigram_probabilities, 1/num_words)
        else:
            bigram_sum = sum(bigram_mapping.get(prev_token).values())
            bigram_number = bigram_mapping.get(prev_token).get(token)
            bigram_probabilities = np.append(bigram_probabilities, bigram_number/bigram_sum)
              
        prev_token = token
        
    #Take the inverse (1/bigram probability)
    inverse_bigram_propabilities = 1/bigram_probabilities
    
    #Get the product of all inverse bigram probabilities
    inverse_bigram_propabilities_product = np.prod(inverse_bigram_propabilities)
    
    #Take to the Nth root
    perplexity = np.power(inverse_bigram_propabilities_product, 1/N)
    return perplexity

#========================================================================

    #Sanity check stuff
def check_add_to_one(dictionary):
    number = 0
    total = 0
    for key in dictionary:
        number += dictionary.get(key)[0]
        total += dictionary.get(key)[1] 
        
    print(number) #Should be N
    print(total)  #Should be very close 1 (will lost a bit from rounding)

def pretty_print_bigram_mapping(bigram_mapping):
    for key in bigram_mapping:
        print(f'{key} -- {bigram_mapping.get(key)}')
        
#========================================================================

#TEST
compute_unigrams_bigrams('data/textbook.txt', 4, 'Nonetheless because perplexity should always be adding probability assigned to save some amount')

