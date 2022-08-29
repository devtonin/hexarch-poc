# Infraestrutura

# AWS
## Mensageria
### Simple Queue Service (SQS)

Funciona como um meio de integração entre microsserviços no qual o microsserviço A entrega uma mensagem ao microserviço B por meio de uma fila.
Para que isto funcione é necessário que o microserviço B consulte a fila onde o microserviço A irá publicar a mensagem, de forma a garantir a sua entrega.
Uma vez consumida a mensagem esta deve ser deletada da fila, pois o SQS assume que o processo de leitura da mensagem na fila pode falhar, e por isso não a deleta
automaticamente após o consumo.

### Comandos AWS CLI para SQS

Lista filas:
``` shell
$ aws --endpoint-url=http://localhost:4566 sqs list-queues
{
    "QueueUrls": [
        "http://localhost:4566/000000000000/to-preparation",
        "http://localhost:4566/000000000000/completed-preparation"
    ]
}
```

Busca atributos de uma fila:
``` shell
$ aws --endpoint-url http://localhost:4566 sqs get-queue-attributes --queue-url http://localhost:4566/000000000000/to-preparation

{
    "Attributes": {
        "ApproximateNumberOfMessages": "3",
        "ApproximateNumberOfMessagesDelayed": "0",
        "ApproximateNumberOfMessagesNotVisible": "0",
        "CreatedTimestamp": "1654093763.143533",
        "DelaySeconds": "0",
        "LastModifiedTimestamp": "1654093763.143533",
        "MaximumMessageSize": "262144",
        "MessageRetentionPeriod": "345600",
        "QueueArn": "arn:aws:sqs:us-east-1:000000000000:to-preparation",
        "ReceiveMessageWaitTimeSeconds": "0",
        "VisibilityTimeout": "30"
    }
}
```

Envia uma mensagem em uma fila:
``` shell
$ aws --endpoint-url=http://localhost:4566 sqs send-message --queue-url http://localhost:4566/000000000000/to-preparation --message-body "Mensagem de teste"
{
    "MD5OfMessageBody": "4449beea32141ebd982d823daa49a542",
    "MessageId": "b54e0e18-dfb0-e42b-bfd4-1de4a5639b1e"
}
```

Busca as últimas 10 mensagens na fila:
``` shell
$ aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/to-preparation --attribute-names All --message-attribute-names All --max-number-of-messages 10
{
    "Messages": [
        {
            "MessageId": "b6062245-9369-fc03-31fa-e729f4947070",
            "ReceiptHandle": "ztyoidqyvszkzjtwygtavatvklttcegttzmjkdhgylcfwgtjgntcxzxkheytyltzvngliwazjdgxpycowtfzaedjzkdjkuuhkgkssrdpckbkqqmfdaxghdzroxlrwalctypjhylwdklsdmnhpfsgqdttmxijdsvfdgjvbrumvpfskbhsbwllrfsag",
            "MD5OfBody": "6192aaab4e508adcada85769a31705eb",
            "Body": "{\"orderId\":\"5d0b687c-b511-41a7-bffa-715bd865c580\",\"orderStatus\":\"COMPLETED\",\"orderItems\":[{\"product\":{\"productId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Cafe\",\"price\":20},\"quantity\":1},{\"product\":{\"productId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Biscoito\",\"price\":60},\"quantity\":3}],\"totalPrice\":200}",
            "Attributes": {
                "SenderId": "AIDAIT2UOQQY3AUEKVGXU",
                "SentTimestamp": "1654093763362",
                "ApproximateReceiveCount": "4",
                "ApproximateFirstReceiveTimestamp": "1654094059208"
            }
        },
        {
            "MessageId": "d547de25-ec9e-b97e-7387-b7fdb778da2c",
            "ReceiptHandle": "lchybdjwyeausjcugwpppneohksgwtlhivfqgmtcodwfiopxlnhbhyuvdrzwcxxncaanzxqimfovuztlahzydsonrnicnrbuvcxhmawqhxyfzsybeeglgqfdpcdxunxoqicpcsdywtzzukqmmabioqlcbsufbckmxkywopiljgulzrvhsupxmucyt",
            "MD5OfBody": "1fa5493da0166a62c9e5bbdba29667b5",
            "Body": "{\"orderId\":\"625738f9-908a-49e5-8f9f-067fb6071a15\",\"orderStatus\":\"COMPLETED\",\"orderItems\":[{\"product\":{\"productId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Cafe\",\"price\":20},\"quantity\":1},{\"product\":{\"productId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Biscoito\",\"price\":60},\"quantity\":3}],\"totalPrice\":200}",
            "Attributes": {
                "SenderId": "AIDAIT2UOQQY3AUEKVGXU",
                "SentTimestamp": "1654093998883",
                "ApproximateReceiveCount": "4",
                "ApproximateFirstReceiveTimestamp": "1654094059209"
            }
        },
        {
            "MessageId": "17ff24a1-c166-bb87-ec5c-d2cb78c0c576",
            "ReceiptHandle": "juvsfnnsiyzjzuxmztgsugvvbqnmvptaqjgakoiznftwevoylesnkvpfvfirgpglxcntginrdsirgjtrlfybcwprulkdyizsjouyivchcteplojznycqyqdwkkglzxqnaqixuwqyakinbvdetcdmvydqjdcxbknhgqpwsucoigktkfrtfvpywwojd",
            "MD5OfBody": "1a2994586066f5e8b2679c46e8b22a66",
            "Body": "{\"orderId\":\"8ad3c72e-7909-4aba-84d2-5ac7695e11a5\",\"orderStatus\":\"COMPLETED\",\"orderItems\":[{\"product\":{\"productId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Cafe\",\"price\":20},\"quantity\":1},{\"product\":{\"productId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"name\":\"Biscoito\",\"price\":60},\"quantity\":3}],\"totalPrice\":200}",
            "Attributes": {
                "SenderId": "AIDAIT2UOQQY3AUEKVGXU",
                "SentTimestamp": "1654094148452",
                "ApproximateReceiveCount": "3",
                "ApproximateFirstReceiveTimestamp": "1654094153479"
            }
        },
        {
            "MessageId": "b54e0e18-dfb0-e42b-bfd4-1de4a5639b1e",
            "ReceiptHandle": "lyksandtapcovgvytqdwzgxecwvgwsplzhbrhzqnlepevrozfupbkxheynguzztgwtopqpweeeuhdwpvqddgntanbljbqtrbvvswstvqvgkajbjfkljyuhqljaarhaazlqobnifgkxreuwvvtdakadzqclnjbarvzkryuonphpugzrefmfzqehbmb",
            "MD5OfBody": "4449beea32141ebd982d823daa49a542",
            "Body": "Mensagem de teste",
            "Attributes": {
                "SenderId": "AIDAIT2UOQQY3AUEKVGXU",
                "SentTimestamp": "1654101281015",
                "ApproximateReceiveCount": "1",
                "ApproximateFirstReceiveTimestamp": "1654101391599"
            }
        }
    ]
}
```

Busca mensagens novas na fila a cada segundo:
``` shell
$ while sleep 1; do aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/to-preparation; done
```

Remove mensagens da fila:
``` shell
$ aws --endpoint-url=http://localhost:4566 sqs purge-queue --queue-url http://localhost:4566/000000000000/to-preparation
```

## Referências
[Documentação AWS CLI](https://docs.aws.amazon.com/cli/latest/reference/sqs/)

[Local Development with AWS on LocalStack](https://reflectoring.io/aws-localstack/)

[How to fake AWS locally with LocalStack](https://dev.to/goodidea/how-to-fake-aws-locally-with-localstack-27me)

[Spring Cloud AWS - Messaging](https://docs.awspring.io/spring-cloud-aws/docs/current/reference/html/index.html#messaging)

[Spring Cloud Messaging With AWS and LocalStack](https://auth0.com/blog/spring-cloud-messaging-with-aws-and-localstack/)


