from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from .models import Note
from .serializers import NoteSerializer


@api_view(['GET'])
def getRoutes(request):
    routes = [
        {'Endpoint': '/notes/', 'method': 'GET', 'body': None, 'description': 'Returns an array of notes'},
        {'Endpoint': '/notes/', 'method': 'POST', 'body': {'body': 'string'}, 'description': 'Creates a new note'},
        {'Endpoint': '/notes/<id>/', 'method': 'GET', 'body': None, 'description': 'Returns a single note'},
        {'Endpoint': '/notes/<id>/', 'method': 'PUT', 'body': {'body': 'string'}, 'description': 'Updates a note'},
        {'Endpoint': '/notes/<id>/', 'method': 'DELETE', 'body': None, 'description': 'Deletes a note'},
    ]
    return Response(routes)


@api_view(['GET', 'POST'])
def getNotes(request):
    if request.method == 'GET':
        notes = Note.objects.all()
        serializer = NoteSerializer(notes, many=True)
        return Response(serializer.data)

    if request.method == 'POST':
        serializer = NoteSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'PUT', 'DELETE'])
def getNote(request, pk):
    try:
        note = Note.objects.get(id=pk)
    except Note.DoesNotExist:
        return Response({'error': 'Note not found'}, status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = NoteSerializer(note)
        return Response(serializer.data)

    if request.method == 'PUT':
        serializer = NoteSerializer(note, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    if request.method == 'DELETE':
        note.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
