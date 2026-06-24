import React, { useState } from 'react';
import { useApp } from '../context/AppContext';
import { Button } from '../components/ui/Button';
import { Input, TextArea, Select } from '../components/ui/Input';
import { Card, CardHeader, CardTitle, CardContent } from '../components/ui/Card';
import { PlusCircle, Upload, CheckCircle, HelpCircle } from 'lucide-react';

export const CreateListing = () => {
  const { createListing, setCurrentPage } = useApp();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState('Steel Scrap');
  const [quantity, setQuantity] = useState('');
  const [unit, setUnit] = useState('Tons');
  const [price, setPrice] = useState('');
  const [location, setLocation] = useState('');
  const [certName, setCertName] = useState('');
  const [success, setSuccess] = useState(false);

  // Preset Unsplash images matching industrial categories to avoid broken layout
  const categoryImages = {
    'Steel Scrap': 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=600&q=80',
    'Fly Ash': 'https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=600&q=80',
    'Plastic Scrap': 'https://images.unsplash.com/photo-1530587191325-3db32d826c18?auto=format&fit=crop&w=600&q=80',
    'Textile Waste': 'https://images.unsplash.com/photo-1524295988550-13f89b910e59?auto=format&fit=crop&w=600&q=80',
    'Wood Waste': 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88?auto=format&fit=crop&w=600&q=80',
    'Glass Waste': 'https://images.unsplash.com/photo-1576086213369-97a306d36557?auto=format&fit=crop&w=600&q=80',
  };

  const handleCreate = (e) => {
    e.preventDefault();
    if (!title || !description || !quantity || !price || !location) {
      alert('Please fill out all mandatory fields.');
      return;
    }

    createListing({
      title,
      description,
      category,
      quantity,
      unit,
      price,
      location,
      image: categoryImages[category] || 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88?auto=format&fit=crop&w=600&q=80',
      certificateUrl: 'quality-vetted-sheet.pdf',
      certificateName: certName || 'Factory Materials Validation Report'
    });

    setSuccess(true);
    setTimeout(() => {
      setSuccess(false);
      setCurrentPage('manage-listings');
    }, 2000);
  };

  return (
    <div className="max-w-2xl mx-auto py-4 animate-fade-in-up">
      <Card className="border-slate-900 shadow-2xl relative">
        <CardHeader className="pb-2">
          <div className="w-10 h-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 flex items-center justify-center mb-3">
            <PlusCircle className="w-5 h-5" />
          </div>
          <CardTitle className="text-xl font-bold">List Waste Materials</CardTitle>
          <p className="text-xs text-slate-400 mt-1">Publish available industrial secondary raw products</p>
        </CardHeader>

        <CardContent className="mt-4">
          {success ? (
            <div className="p-6 bg-emerald-500/10 border border-emerald-500/20 rounded-xl text-center space-y-3">
              <div className="w-12 h-12 rounded-full bg-emerald-500/20 border border-emerald-500/40 flex items-center justify-center mx-auto text-emerald-400">
                <CheckCircle className="w-6 h-6 animate-bounce" />
              </div>
              <h3 className="text-base font-bold text-slate-200">Listing Created successfully!</h3>
              <p className="text-xs text-slate-400 max-w-sm mx-auto leading-relaxed">
                Your material posting is active in the B2B marketplace. Moving to stock manager...
              </p>
            </div>
          ) : (
            <form onSubmit={handleCreate} className="space-y-4">
              <Input
                label="Material Title / Grade"
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Grade 1 Shredded Scrap Automotive Sheet"
                required
              />

              <TextArea
                label="Technical Description & Contamination Levels"
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="High-density shredded scrap metal sheet. Sourced post-manufacturing. Contamination level: < 0.2%. Non-hazardous."
                rows={3}
                required
              />

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <Select
                  label="Material category"
                  id="category"
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  options={[
                    { label: 'Steel Scrap', value: 'Steel Scrap' },
                    { label: 'Fly Ash', value: 'Fly Ash' },
                    { label: 'Plastic Scrap', value: 'Plastic Scrap' },
                    { label: 'Textile Waste', value: 'Textile Waste' },
                    { label: 'Wood Waste', value: 'Wood Waste' },
                    { label: 'Glass Waste', value: 'Glass Waste' }
                  ]}
                />

                <div className="grid grid-cols-2 gap-2">
                  <Input
                    label="Volume"
                    id="quantity"
                    type="number"
                    step="0.1"
                    value={quantity}
                    onChange={(e) => setQuantity(e.target.value)}
                    placeholder="45"
                    required
                  />
                  <Select
                    label="Unit"
                    id="unit"
                    value={unit}
                    onChange={(e) => setUnit(e.target.value)}
                    options={[
                      { label: 'Tons', value: 'Tons' },
                      { label: 'Lbs', value: 'Lbs' },
                      { label: 'Supersacks', value: 'Supersacks' }
                    ]}
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <Input
                  label="Base Price (USD per unit)"
                  id="price"
                  type="number"
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  placeholder="320"
                  required
                />

                <Input
                  label="Origin Hub (City, State)"
                  id="location"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                  placeholder="Gary, IN"
                  required
                />
              </div>

              {/* Certificate Input */}
              <div className="space-y-2 border border-slate-800 rounded-lg p-4 bg-slate-950/20">
                <h4 className="text-xs font-bold text-slate-300 uppercase tracking-wider">
                  Quality Certificates validation
                </h4>
                
                <Input
                  label="Certificate Title / Standard Code"
                  id="certName"
                  value={certName}
                  onChange={(e) => setCertName(e.target.value)}
                  placeholder="ASTM E1019 Steel Chemical Assay Sheet"
                />

                <div className="border border-dashed border-slate-800 rounded-lg p-4 flex flex-col items-center justify-center bg-slate-950/40 text-center relative cursor-pointer hover:border-slate-700 transition-all">
                  <input type="file" className="absolute inset-0 opacity-0 cursor-pointer" />
                  <Upload className="w-5 h-5 text-slate-500 mb-1" />
                  <span className="text-xs font-semibold text-slate-300">
                    Upload Regulatory Verification Sheet
                  </span>
                  <span className="text-[9px] text-slate-500">Attach laboratory analysis PDF</span>
                </div>
              </div>

              {/* Legal Notice */}
              <div className="flex gap-2 p-3 bg-indigo-950/20 border border-indigo-500/20 text-slate-400 text-xs rounded-lg">
                <HelpCircle className="w-4 h-4 text-indigo-400 shrink-0 mt-0.5" />
                <p className="leading-normal">
                  Listings are reviewed by admins before going live on the marketplace.
                </p>
              </div>

              <Button type="submit" variant="primary" className="w-full mt-2">
                Publish Material Listing
              </Button>
            </form>
          )}
        </CardContent>
      </Card>
    </div>
  );
};
