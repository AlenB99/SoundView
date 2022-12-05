import openai
import os


def image_generate(x):
    openai.api_key = "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"
    response = openai.Image.create(
        prompt=str(x),
        n=4,
        size="1024x1024"
    )
    image_url1 = response['data'][0]['url']
    image_url2 = response['data'][1]['url']
    image_url3 = response['data'][2]['url']
    image_url4 = response['data'][3]['url']
    image_urls= [image_url1,image_url2,image_url3,image_url4]
    return image_urls


