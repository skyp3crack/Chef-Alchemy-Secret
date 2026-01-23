import api from "./api";


export interface Tag {
    id: number;
    name: string;
}

export interface Category {
    id: number;
    name: string;
}


export interface Rating {
    id?: number;
    userId?: number;
    recipeId?: number;
    score: number;
    createdAt?: string;
    updatedAt?: string;
}

export interface Review {
    id?: number;
    userId?: number;
    username?: string;
    recipeId?: number;
    comment: string;
    createdAt?: string;
    updatedAt?: string;
}

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
    tags?: string[];
    categories?: string[];
    tagIds?: number[];
    categoryIds?: number[];
    averageRating?: number
    ratingCount?: number
    reviews?: Review[];//list of review objects
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

    async getAllCategories(): Promise<Category[]> {
        try {
            const response = await api.get<Category[]>('/categories')
            return response.data
        } catch (error: any) {
            throw new Error(error.response?.data || 'Failed to fetch categories')
        }
    }

    async getAllTags(): Promise<Tag[]> {
        try {
            const response = await api.get<Tag[]>('/tags')
            return response.data
        } catch (error: any) {
            throw new Error(error.response?.data || 'Failed to fetch tags')
        }
    }


    async submitRating(recipeId: number, score: number): Promise<string> {
        try {
            const response = await api.post(`/recipes/${recipeId}/ratings`, { score });
            return response.data;
        } catch (error: any) {
            throw new Error(error.response?.data || 'Failed to submit rating');
        }
    }

    async getUserRatingForRecipe(recipeId: number): Promise<number> {
        try {
            const response = await api.get(`/recipes/${recipeId}/ratings/my`);
            return response.data;
        } catch (error: any) {
            if (error.response && error.response.status === 404) {
                return 0;
            }
            throw new Error(error.response?.data || 'Failed to fetch user rating');
        }
    }


    async addReview(recipeId: number, comment: string): Promise<Review> {
        try {
            const response = await api.post(`/recipes/${recipeId}/reviews`, { comment });
            return response.data;
        } catch (error: any) {
            throw new Error(error.response?.data || 'Failed to add review');
        }
    }

    async updateReview(recipeId: number, reviewId: number, comment: string): Promise<Review> {
        try {
            const response = await api.put<Review>(`/recipes/${recipeId}/reviews/${reviewId}`, { comment })
            return response.data
        } catch (error: any) {
            throw new Error(error.response?.data || 'Failed to update review')
        }
    }

    async deleteReview(recipeId: number, reviewId: number): Promise<void> {
        try {
            await api.delete(`/recipes/${recipeId}/reviews/${reviewId}`)
        } catch (error: any) {
            throw new Error(error.response?.data || 'Failed to delete review')
        }
    }
}

export default new RecipeService()