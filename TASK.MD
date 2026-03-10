# cv6 — Django Notes REST API

## Context

Course: XPC-MMA (Mobile and Multi-platform Applications), VUT Brno 2024/25  
Branch: `cv6`  
Platform: Python 3 + Django + Django REST Framework  
IDE: PyCharm Community Edition (macOS, Apple Silicon)  
Goal: Build a backend REST API for a Flutter Notes app

This document is a self-contained spec. Implement everything described here from scratch.
Do not assume any prior state beyond a clean `cv6` branch with only `.gitignore`, `README.md`, and `CLAUDE.md`.

---

## Deliverables Summary

| Points | Requirement |
|--------|-------------|
| 3b | Virtual environment created, Django installed, server runs |
| 7b | Full CRUD REST API for Notes (list, create, get, update, delete) |

---

## 1. Project Structure (target)

```
cv6/
├── env/                        # Python virtual environment — NOT committed
├── notes/                      # Django project root (generated)
│   ├── settings.py
│   ├── urls.py
│   ├── wsgi.py
│   └── asgi.py
├── api/                        # Django app (generated)
│   ├── migrations/
│   ├── admin.py
│   ├── apps.py
│   ├── models.py               # Note model
│   ├── serializers.py          # NoteSerializer (create this file manually)
│   ├── views.py                # all endpoint handlers
│   └── urls.py                 # app-level URL routing (create this file manually)
├── manage.py
└── requirements.txt            # pin versions here after install
```

---

## 2. Environment Setup

### Create and activate virtual environment

```bash
python3 -m venv env
source env/bin/activate
```

### Install dependencies

```bash
pip install django djangorestframework
```

### Save dependencies

```bash
pip freeze > requirements.txt
```

### Generate Django project and app

```bash
django-admin startproject notes .
python manage.py startapp api
```

Note: the `.` in `startproject` places `manage.py` at the repo root rather than nesting an extra directory.

---

## 3. Django Settings (`notes/settings.py`)

Make two changes:

**Add `api` and `rest_framework` to `INSTALLED_APPS`:**

```python
INSTALLED_APPS = [
    # ...existing entries...
    'rest_framework',
    'api.apps.ApiConfig',
]
```

No other settings changes are required. Leave `DATABASES` as the default SQLite config.

---

## 4. Note Model (`api/models.py`)

```python
from django.db import models


class Note(models.Model):
    body = models.TextField()
    updated = models.DateTimeField(auto_now=True)
    created = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.body[0:50]

    class Meta:
        ordering = ['-updated']
```

After defining the model, run migrations:

```bash
python manage.py makemigrations
python manage.py migrate
```

---

## 5. Admin Registration (`api/admin.py`)

Register the Note model so it is visible in the Django admin panel at `/admin/`.

```python
from django.contrib import admin
from .models import Note

admin.site.register(Note)
```

Create a superuser to access the admin panel:

```bash
python manage.py createsuperuser
```

---

## 6. Serializer (`api/serializers.py`)

Create this file — it does not exist by default.

```python
from rest_framework.serializers import ModelSerializer
from .models import Note


class NoteSerializer(ModelSerializer):
    class Meta:
        model = Note
        fields = '__all__'
```

---

## 7. Views (`api/views.py`)

Implement all five endpoints. Use `@api_view` decorators from Django REST Framework.

### Endpoint overview

| Method | URL | View function | Description |
|--------|-----|---------------|-------------|
| GET | `/` | `getRoutes` | List all available API routes |
| GET | `/notes/` | `getNotes` | Return all notes |
| POST | `/notes/` | `getNotes` | Create a new note |
| GET | `/notes/<id>/` | `getNote` | Return a single note by ID |
| PUT | `/notes/<id>/` | `updateNote` | Update a note by ID |
| DELETE | `/notes/<id>/` | `deleteNote` | Delete a note by ID |

### Full implementation

```python
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
```

---

## 8. URL Routing

### App-level URLs (`api/urls.py`)

Create this file — it does not exist by default.

```python
from django.urls import path
from . import views

urlpatterns = [
    path('', views.getRoutes),
    path('notes/', views.getNotes),
    path('notes/<str:pk>/', views.getNote),
]
```

### Project-level URLs (`notes/urls.py`)

Wire the app URLs into the project:

```python
from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('api.urls')),
]
```

---

## 9. Running the Server

```bash
source env/bin/activate
python manage.py runserver
```

Server runs at `http://127.0.0.1:8000/`

### Verify these URLs respond correctly

| URL | Expected |
|-----|----------|
| `http://127.0.0.1:8000/` | JSON list of routes |
| `http://127.0.0.1:8000/notes/` | Empty array `[]` or list of notes |
| `http://127.0.0.1:8000/admin/` | Django admin login page |

The Django REST Framework browsable API (HTML view) is available in-browser for all endpoints.

---

## 10. What NOT to do

- Do not commit the `env/` directory — it is already in `.gitignore`
- Do not commit `db.sqlite3` — already in `.gitignore`
- Do not change the default SQLite database config — not required for this task
- Do not add CORS headers — Flutter integration is out of scope for this exercise
- Do not add authentication — not required for this task