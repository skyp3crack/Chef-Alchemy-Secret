import api from "./api";

export interface Recipe {
    id?: number;
    title: string;
    description: string;
    ingredients: string;
    instructions: string;
    imageUrl?: string;
    authorUsername?: string;
    authorId?: number;
    createdAt?: string;
    updatedAt?: string;
}

class RecipeService {

    async getAllRecipes(): Promise<Recipe[]> {
        try {
            const response = await api.get<Recipe[]>('/recipes')
            return response.data
        } catch (e: any) {
            throw new Error(e.response.data.message || 'Failed to fetch recipes');
        }
    }

    async getRecipeById(id: number): Promise<Recipe> {
        try {
            const response = await api.get<Recipe>(`/recipes/${id}`)
            return response.data
        } catch (e: any) {
            throw new Error(e.response.data.message || `Failed to fetch recipe with id ${id}`);
        }
    }

    async createRecipe(recipe: Recipe): Promise<Recipe> {
        try {
            const response = await api.post<Recipe>(`/recipes`, recipe)
            return response.data
        } catch (e: any) {
            throw new Error(e.response.data.message || 'Failed to create recipe');
        }
    }

    async updateRecipe(recipe: Recipe): Promise<Recipe> {
        try {
            const response = await api.put<Recipe>(`/recipes/${recipe.id}`, recipe)
            return response.data
        } catch (e: any) {
            throw new Error(e.response.data.message || `Failed to update recipe with id ${recipe.id}`);
        }
    }

    async deleteRecipe(id: number): Promise<void> {
        try {
            await api.delete(`/recipes/${id}`)
        } catch (e: any) {
            throw new Error(e.response.data.message || `Failed to delete recipe with id ${id}`);
        }
    }

}

export default new RecipeService()