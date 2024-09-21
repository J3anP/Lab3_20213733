package com.example.pomodoro;

public class TareaStats {
    private Tarea[] todos;
    private int total;
    private int skip;
    private int limit;

    public Tarea[] getTodos() {
        return todos;
    }

    public void setTodos(Tarea[] todos) {
        this.todos = todos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
