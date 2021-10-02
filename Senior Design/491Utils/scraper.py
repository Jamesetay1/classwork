import requests
from bs4 import BeautifulSoup as bs
import json

def get_question_text(question_name):
    data = {"operationName":"questionData","variables":{"titleSlug":question_name},"query":"query questionData($titleSlug: String!) {\n  question(titleSlug: $titleSlug) {\n    questionId\n    questionFrontendId\n    boundTopicId\n    title\n    titleSlug\n    content\n    translatedTitle\n    translatedContent\n    isPaidOnly\n    difficulty\n    likes\n    dislikes\n    isLiked\n    similarQuestions\n    contributors {\n      username\n      profileUrl\n      avatarUrl\n      __typename\n    }\n    langToValidPlayground\n    topicTags {\n      name\n      slug\n      translatedName\n      __typename\n    }\n    companyTagStats\n    codeSnippets {\n      lang\n      langSlug\n      code\n      __typename\n    }\n    stats\n    hints\n    solution {\n      id\n      canSeeDetail\n      __typename\n    }\n    status\n    sampleTestCase\n    metaData\n    judgerAvailable\n    judgeType\n    mysqlSchemas\n    enableRunCode\n    enableTestMode\n    envInfo\n    libraryUrl\n    __typename\n  }\n}\n"}

    r = requests.post('https://leetcode.com/graphql', json = data).json()
    html_question = r['data']['question']['content']
    soup = bs(r['data']['question']['content'], 'lxml')
    title = r['data']['question']['title']
    plain_question =  soup.get_text().replace('\n',' ')
    return [title, html_question, plain_question]

problem_names = ["two-sum", "add-two-numbers", "longest-substring-without-repeating-characters", "median-of-two-sorted-arrays", "longest-palindromic-substring", "zigzag-conversion", "reverse-integer", "string-to-integer-atoi", "palindrome-number", "regular-expression-matching", "container-with-most-water", "integer-to-roman", "roman-to-integer", "longest-common-prefix", "3sum", "3sum-closest", "letter-combinations-of-a-phone-number", "4sum", "remove-nth-node-from-end-of-list", "merge-two-sorted-lists", "generate-parentheses", "merge-k-sorted-lists", "swap-nodes-in-pairs", "reverse-nodes-in-k-group", "remove-duplicates-from-sorted-array" , "remove-element", "implement-strstr", "divide-two-integers","substring-with-concatenation-of-all-words", "next-permutation","longest-valid-parentheses", "search-in-rotated-sorted-array","find-first-and-last-position-of-element-in-sorted-array", "search-insert-position","valid-sudoku", "sudoku-solver","count-and-say", "combination-sum","combination-sum-ii", "first-missing-positive","trapping-rain-water", "multiply-strings","wildcard-matching", "jump-game-ii","permutations", "permutations-ii","rotate-image", "group-anagrams","powx-n", "n-queens"]
all_problems = list(map(get_question_text, problem_names))

#Problem 0, all info
print(len(all_problems))
for i in range(len(all_problems)):
    text_file = open(f"scrapedhtml/{all_problems[i][0]}.html", "x", encoding="utf-8")
    text_file.write(all_problems[i][1])
    text_file.close()
    
    #text_file = open(f"scrapedtxt/{all_problems[i][0]}.txt", "x", encoding="utf-8")
    #text_file.write(all_problems[i][2])
    #text_file.close()