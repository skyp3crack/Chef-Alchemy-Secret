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
    imageUrl?: string | null;
    imageFile?: File | null;
    authorUsername?: string;
    authorId?: number;
    createdAt?: string;
    updatedAt?: string;
    tags?: Tag[];
    categories?: Category[];
    tagIds?: number[];
    categoryIds?: number[];
    averageRating?: number
    ratingCount?: number
    reviews?: Review[];//list of review objects
}

class RecipeService {
    private buildFormData(recipe: Recipe): FormData {
        const formData = new FormData();
        //create json object for the recipe data without file
        const recipeData = {
            title: recipe.title,
            description: recipe.description,
            ingredients: recipe.ingredients,
            instructions: recipe.instructions,
            tagIds: recipe.tagIds,
            categoryIds: recipe.categoryIds,
            removeImage: recipe.imageUrl === null, // Signal deletion to backend
        }
        // Backend expects 'recipe' part as application/json
        formData.append('recipe', new Blob([JSON.stringify(recipeData)], { type: 'application/json' }));

        // Backend expects 'image' part (as defined in RecipeRequest and Controller)
        if (recipe.imageFile) {
            formData.append('image', recipe.imageFile);
        } else if (recipe.imageUrl === null) {
            // Signal deletion to backend
            // Note: removeImage flag in recipeData handles this, but we can also send an empty blob
            formData.append('image', new Blob([], { type: 'application/octet-stream' }));
        }
        return formData;
    }



    async getAllRecipes(keyword?: string, categoryIds?: number[], tagIds?: number[]): Promise<Recipe[]> {
        const params: any = {}; // any is used to define a type that can be any type . and {} is used to define an object

        if (keyword) params.keyword = keyword;
        if (categoryIds && categoryIds.length > 0) params.categoryIds = categoryIds?.join(','); //join is used to join the array elements into a string
        if (tagIds && tagIds.length > 0) params.tagIds = tagIds?.join(',');
        try {
            const response = await api.get<Recipe[]>('/recipes', { params })//{params} as parameters to pass to the backend
            return response.data;
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
            const formData = this.buildFormData(recipe);
            const response = await api.post<Recipe>(`/recipes`, formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'//for file upload with form data
                    }
                }
            );
            return response.data;
        } catch (e: any) {
            throw new Error(e.response.data.message || 'Failed to create recipe');
        }
    }

    async updateRecipe(recipe: Recipe): Promise<Recipe> {
        try {
            const formData = this.buildFormData(recipe);
            const response = await api.put<Recipe>(`/recipes/${recipe.id}`, formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data'//for file upload with form data
                    }
                }
            );
            return response.data;
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